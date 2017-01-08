package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Main;
import model.ImageModel;
import model.ImageModelJSON;
import view.ImageView;

public class EncryptionWindow {

	private JFrame frame;
	private JTextField passwordTextField;
	private ImageView view;
	private ArrayList<Rectangle> rectangles;
	private String password;
	private ImageModelJSON imageModelJSON;
	private ImageModel model;
	private String fileName;
	private String path;
	private SecretKey sessionKey;

	/**
	 * setVisible affiche ou cache la fenetre frame
	 * 
	 * @param state
	 *            boolean : true permet d'afficher la fenetre frame et false la
	 *            cache
	 */
	public void setVisible(boolean state) {
		frame.setVisible(state);
	}

	/**
	 * Create the application.
	 * 
	 * @param view
	 */
	public EncryptionWindow(ImageView view, String fileName, String path, ImageModel model) {
		this.fileName = fileName;
		this.path = path;
		this.model = model;
		this.view = view;
		this.rectangles = this.view.getRectangles();
		this.imageModelJSON = new ImageModelJSON();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Encryption");
		frame.setSize(300, 50);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);

		JLabel passwordLabel = new JLabel("Enter password :");
		passwordLabel.setBounds(31, 83, 138, 36);
		frame.getContentPane().add(passwordLabel);

		passwordTextField = new JTextField();
		passwordTextField.setBounds(193, 83, 208, 36);
		frame.getContentPane().add(passwordTextField);
		passwordTextField.setColumns(10);

		JButton encryptButton = new JButton("Encrypt");
		encryptButton.setBounds(251, 142, 105, 36);
		frame.getContentPane().add(encryptButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(251, 189, 105, 36);
		frame.getContentPane().add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		encryptButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {
				System.out.println("debut crypto");

				if (passwordTextField.getText() != null)
					password = passwordTextField.getText();

				if (model != null) {
					BufferedImage image = model.getImage();

					// Recuperer les rectangles de la vue
					rectangles = view.getPreparedRectangles();

					// le String correspondant aux donnees a crypter
					String rgbString = getRGBToString(image);
					System.out.println(rgbString);

					// avec RSA, mdp -> cle session KEYGENERATOR ou SECRETKEY ?
					SecretKey aesKey = null;
					try {
						aesKey = encryptionPassword(password);
					} catch (NoSuchAlgorithmException | InvalidKeySpecException e3) {
						e3.printStackTrace();
					}

					// Genere une paire de cles

					KeyPairGenerator kpg;

					try {
						kpg = KeyPairGenerator.getInstance("RSA");
						kpg.initialize(512);
						KeyPair kp = kpg.generateKeyPair();

						PublicKey pubk = kp.getPublic();
						PrivateKey prvk = kp.getPrivate();

						byte[] dataBytes = aesKey.getEncoded();
						// Creation du tableau d'octets chiffre par RSA
						byte[] encBytes = encrypt(dataBytes, pubk, "RSA");

						String sessionKey = convert(encBytes);
						String privateKey = convert(prvk.getEncoded());
						String publicKey = convert(pubk.getEncoded());

						// avec AES et cle de session, crypte les donnees de
						// limage
						// (=vecteur rgbs)
						byte[] encryptedBytes = encryptionData(aesKey, rgbString);

						// stocker le vecteur et la cle de session dans le json
						String encryptedString = convert(encryptedBytes);

						// recree l'image avec les RGB originaux
						/*
						 * File outputfile = new File("saved.jpg"); try {
						 * ImageIO.write(createBufferedImage(rgbs,
						 * image.getWidth(), image.getHeight()), "jpg",
						 * outputfile); } catch (IOException e2) {
						 * e2.printStackTrace(); }
						 */

						String[] json = null;
						// Getting filePath and fileName from MainWindow
						// imageModelJSON.writeImageModelJSONFile(Main.filePath,
						// Main.fileName, password, encryptedString);
						json = imageModelJSON.writeImageModelJSONFile(path, fileName, sessionKey, encryptedString,
								publicKey, privateKey);
						
						String extension = fileName.split("\\.")[1];
						model.saveIMG(path, extension);
						
						String jsonFolder = json[0];
						System.out.println("\nJSON FOLDER PATH : " + jsonFolder);
						String jsonName = json[1];
						System.out.println("\nJSON NAME : " + jsonName);
						String imageFolderPath = path.split(fileName)[0];
						System.out.println("\nIMAGE FOLDER PATH : " + imageFolderPath);
						System.out.println("\nFILE NAME : " + fileName);
						hideZipInImage(imageFolderPath, fileName, jsonFolder, jsonName);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (NoSuchAlgorithmException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/*
					 * for(Rectangle rectangle : rectangles){
					 * model.pixelateImage(rectangle);
					 * 
					 * }
					 */
					

				}

				// TODO

				/*
				 * // Recuperer un emplacement commun a plusieurs os et
				 * l'utiliser // pour le stockage de donnees String location =
				 * System.getProperty("user.home"); File file = new
				 * File(location, "Test.txt");
				 * 
				 * try { // Ecrire des octets dans un fichier //
				 * FileOutputStream fout = new FileOutputStream(file);
				 * PrintWriter pw = new PrintWriter(file);
				 * 
				 * pw.write("test"); pw.close(); } catch (FileNotFoundException
				 * e1) { e1.printStackTrace(); }
				 */

				// Gestion du mot de passe
				/*
				 * if (passwordTextField.getText().equals("password"))
				 * frame.setContentPane(new ResultPanel(frame,
				 * "Encryption successed")); else frame.setContentPane(new
				 * ResultPanel(frame, "Encryption failed")); frame.revalidate();
				 */
				frame.setVisible(false);
				frame.dispose();
			}

		});

	}

	// TODO

	/**
	 * encryptionPassword genere une clef avec l'algorithme xxxxx en utilisant
	 * le password
	 * 
	 * @param password
	 *            String qui est crypte avec l'algorithme xxxx
	 * @return SecretKey aesKey la clef genere par l'algorithme xxxx depuis le
	 *         password
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */

	private byte[] encrypt(byte[] inpBytes, PublicKey key, String xform) throws Exception {
		Cipher cipher = Cipher.getInstance(xform);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(inpBytes);
	}

	private SecretKey encryptionPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		char[] pswd = password.toCharArray();
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3,
				(byte) 0x03 };
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(pswd, salt, 65536, 128);
		this.sessionKey = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(sessionKey.getEncoded(), "AES");
		return secret;
	}

	// TODO : javadoc
	/*
	 * public byte[] encryptionPassword2(String password, PublicKey publicKey)
	 * throws Exception { Cipher cipher =
	 * Cipher.getInstance("RSA/NONE/PKCS1PADDING");
	 * cipher.init(Cipher.ENCRYPT_MODE, publicKey); return
	 * cipher.doFinal(password.getBytes()); }
	 */

	// TODO
	// est-ce necessaire ???????????????????????????????????????????
	/**
	 * Convertit une SecretKey en String
	 * 
	 * @param key
	 *            SecretKey a convertir
	 * @return String s la conversion du tableau de Bytes
	 */
	private String convert(SecretKey key)// byte[] encryptedBytes)
	{
		// Get string representation of byte array of SecretKey
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	private String convert(byte[] encryptedBytes) {
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/**
	 * encryptionData crypte le rgbString avec l'algorithme AES en utilisant la
	 * clef aesKey
	 * 
	 * @param aesKey
	 *            SecretKey la clef utilisee lors de l'algorithme AES pour
	 *            crypter rgbString
	 * @param rgbString
	 *            String qui est crypte avec l'algorithme AES et aesKey
	 * @return byte[] encryptedBytes qui est le tableau de Bytes correspondant a
	 *         rgbString crypte
	 */
	private byte[] encryptionData(SecretKey aesKey, String rgbString) {
		String s = rgbString;// rgbtoString(rgbString, image.getWidth(),
								// image.getHeight());
		byte[] encryptedBytes = null;
		try {
			// Encrypt cipher
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
			// Encrypt
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
			cipherOutputStream.write(s.getBytes());
			cipherOutputStream.flush();
			cipherOutputStream.close();
			encryptedBytes = outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedBytes;
	}

	/**
	 * getRGBToString genere le String a crypter : "/" pour les pixels a ne pas
	 * crypter et la valeur en Bytes pour les pixels a crypter
	 * 
	 * @param image
	 *            BufferedImage l'image a crypter
	 * @return String res correspondant aux donnees de l'image a crypter
	 */
	public String getRGBToString(BufferedImage image) {
		Random rand = new Random();
		String res = "";
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				boolean isIn = false;
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						isIn = true;
						break;
					}
				}
				if (isIn) {
					res += String.valueOf(image.getRGB(i, j));
					/* Create random color for pixel */
					// Color color = new Color(rand.nextInt(255),
					// rand.nextInt(255), rand.nextInt(255));
					int alpha = 255;
					int red = rand.nextInt(255);
					int green = rand.nextInt(255);
					int blue = rand.nextInt(255);
					int p = (alpha << 24) | (red << 16) | (green << 8) | blue;

					/* Change pixel(i, j) color */
					// image.setRGB(i, j, color.getRGB());
					image.setRGB(i, j, p);
				} else
					res += "0";
				res += "/";
			}
		}
		return res;
	}

	/**
	 * createBufferedImage genere une image a partir d'un tableau de pixels et
	 * des dimensions de l'image
	 * 
	 * @param rgbs
	 *            int[][] le tableau de pixels de l'image
	 * @param width
	 *            int la largeur de l'image
	 * @param height
	 *            int la hauteur de l'image
	 * @return BufferedImage buff est l'image reconstituee avec les paramètres
	 *         d'entree
	 */
	public static BufferedImage createBufferedImage(int[][] rgbs, int width, int height) {
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// System.out.println("i = " + i + " j = " + j + " RGB = " +
				// image.getRGB(i, j));
				buff.setRGB(i, j, rgbs[i][j]);
			}
		}
		return buff;

		/*
		 * File outputfile = new File("saved.png"); ImageIO.write(bi, "png",
		 * outputfile);
		 */
	}

	/**
	 * Cette fonction sert a cacher un fichier json dans un zip, lui-meme cache
	 * dans une image. <br>
	 * ATTENTION : Supprime le zip ainsi que le fichier json !
	 * 
	 * @param imageFolder
	 *            Contient le path vers le dosier contenant l'image
	 * @param imageName
	 *            Contient le nom de l'image (extension incluse)
	 * @param jsonFolder
	 *            Contient le path vers le dosier contenant le json
	 * @param jsonName
	 *            Contient le nom du fichier json (extension .json incluse)
	 * @throws IOException
	 */
	public void hideZipInImage(String imageFolder, String imageName, String jsonFolder, String jsonName)
			throws IOException {
		// Creer un zip avec le json dedans
		File imageFile = new File(imageFolder + imageName);
		File jsonFile = new File(jsonFolder + jsonName);
		imageFolder = imageFile.getAbsolutePath().split(imageName)[0];
		jsonFolder = jsonFile.getAbsolutePath().split(jsonName)[0];

		FileInputStream in = new FileInputStream(jsonFolder + jsonName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(imageFolder + "json.zip"));
		out.putNextEntry(new ZipEntry(jsonName));
		byte[] b = new byte[1024];
		int count;
		while ((count = in.read(b)) > 0) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();

		String os = System.getProperty("os.name").toLowerCase();// OS detection

		if ((os.indexOf("win") >= 0)) { // WINDOWS
			try {
				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
						"cd \"" + imageFolder + "\" && copy /b " + imageName + "+json.zip " + imageName);
				builder.redirectErrorStream(true);
				Process p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while (true) {
					line = r.readLine();
					if (line == null) {
						break;
					}
					// System.out.println(line);
				}
			} catch (Exception ex) {
			}
		} else if (os.indexOf("mac") >= 0) { // MAC OS

		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) { // UNIX-LINUX
			try {
				String command = "cd " + imageFolder + " && cat " + imageName + " json.zip > temp && cat temp > "
						+ imageName + " && rm temp";
				System.out.println(command);
				String s;
				Process p;
				try {
					p = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((s = br.readLine()) != null)
						// System.out.println("line: " + s); //Sortie de la
						// commande
						p.waitFor();
					// System.out.println ("exit: " + p.exitValue());//Code de
					// sortie de la commande
					p.destroy();
				} catch (Exception e) {
				}
			} catch (Exception ex) {
			}
		} else {
			System.out.println("Your OS is not supported!!");
		}

		// Supprimer le zip
		try {
			Files.deleteIfExists(Paths.get(imageFolder + "json.zip"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Supprimer le json d'origine !
		try {
			Files.deleteIfExists(Paths.get(imageFolder + jsonName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}