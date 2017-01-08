package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.parser.ParseException;

import model.ImageModel;
import model.ImageModelJSON;
import view.ImageView;

public class DecryptionWindow {

	private JFrame frame;
	private JTextField passwordTextField;
	private ImageView view;
	private ArrayList<Rectangle> rectangles;
	private String password;
	private ImageModelJSON imageModelJSON;
	private ImageModel model;
	private String fileName;
	private String path;
	private SecretKey passwordKey;

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
	 */
	public DecryptionWindow(ImageView view, String fileName, String path, ImageModel model) {
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
		frame.setTitle("Decryption");
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

		JButton decryptButton = new JButton("Decrypt");
		decryptButton.setBounds(251, 142, 105, 36);
		frame.getContentPane().add(decryptButton);

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

		decryptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// * DECRYPTAGE
				String folder = path.split(fileName)[0];
				findZipInImage(folder, fileName);
				// recuperer le mdp et limage
				if (passwordTextField.getText() != null)
					password = passwordTextField.getText();

				ImageModel imageModel = null;

				// recuperer les valeurs dans le json
				String[] jsonString = null;
				try {
					jsonString = imageModelJSON.readImageFromJson(folder+"json.json", "json.json");
				
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				String filePath = jsonString[0];
				String file_Name = jsonString[1];
				String encryptedString = jsonString[2];
				String publicKey = jsonString[3];
				String privateKey = jsonString[4];
				String sessionKey = jsonString[5];
				//System.out.println("filePath : " + filePath + "\nfileName : " + file_Name + "\nsessionKey : " + encryptedString + "\npublicKey : " + publicKey + "\nprivateKey : " + privateKey);

				// avec RSA, verifier que le mdp = cle de session
				try {
					// passwordKey est une SecretKey
					passwordKey = encryptionPassword(password);
					byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
					KeyFactory kf = KeyFactory.getInstance("RSA");
					PrivateKey pk = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
					byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
					byte[] encBytes = decrypt(sessionKeyBytes, pk);
					SecretKey passWordKeyFromJson = new SecretKeySpec(encBytes, "AES");
					String test2 = convert(passWordKeyFromJson);
					String test = convert(passwordKey);
					// System.out.println("sessionKey : " + test + "\npasswordKey : " + test2 + "\nequals : " + test.equals(test2));
					if(test.equals(test2))
					{
						// byte[] encryptedStringBytes = Base64.getDecoder().decode(encryptedString);
						// String result = decryptionData(passwordKey, encryptedStringBytes);
						String initVector = "RandomInitVector";
						String result = newDecrypt(password, initVector, encryptedString, passwordKey);
						// System.out.println("test : " + test);
						// System.out.println("encryptedString : " + encryptedString);
						System.out.println("result : " + result);
						model.rebuildImage(result);
						view.repaint();
						//System.out.println(path);
						//System.out.println(fileName.split("\\.")[1]);
						model.saveIMG(path, fileName.split("\\.")[1]);
					}
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
					e2.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				byte[] dataBytes = passwordKey.getEncoded();
				byte[] decodedKey = Base64.getDecoder().decode(dataBytes);
				SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
				*/
				/*
				//SecretKey publicKey = convertStringToSecretKey(sessionKey);
				SecretKey key = new SecretKeySpec(convert(publicKey), 0, convert(publicKey).length, "AES");
				SecretKey secretKey = encryptionPassword(password, key);
				
				// convertit le String crypte en tableau de bytes cryptes
				byte[] encryptedBytes = convertStringToBytes(encryptedString);
				
				// avec AES et la cle de session, decrypter le tableau de bytes en String 
				String decryptedString = decryptionData(secretKey, encryptedBytes);
				
				// convertir le String en matrice de pixels
				int rgbs[][] = convertStringToInts(decryptedString);
				
				// sauvegarder limage et supprimer le json
				BufferedImage bufferedImage = createBufferedImage(rgbs, imageModel.getImage().getWidth(), imageModel.getImage().getHeight());
				saveImage(bufferedImage);
				*/
				try {
					deleteJSON(folder+"json.json");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				/*
				 * // Gestion du mot de passe if
				 * (passwordTextField.getText().equals("password"))
				 * frame.setContentPane(new ResultPanel(frame,
				 * "Decryption successed")); else frame.setContentPane(new
				 * ResultPanel(frame, "Decryption failed")); frame.revalidate();
				 */
				frame.setVisible(false);
				frame.dispose();
			}
		});
	}
	
	public String newDecrypt(String key, String initVector, String encrypted, SecretKey aesKey) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, iv);

			byte[] original = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * Cette fonction sert a trouver un fichier json dans un zip, lui-meme cache dans une image, puis l'extraire.
	 * @param imageFolder
	 * Contient le path vers le dosier contenant l'image
	 * @param imageName
	 * Contient le nom de l'image (extension incluse)
	 */
	public void findZipInImage(String imageFolder, String imageName)
	{
		//Renommer image.bmp en image.zip
		String name = imageName.split("\\.")[0];
		String extension = imageName.split("\\.")[1];
		File imgFile  = new File(imageFolder+name+"."+extension);
		File zipFile1 = new File(imageFolder+name+".zip");
		imgFile.renameTo(zipFile1);

		//Recuperer le contenu du zip (un json) et le copier a part
		ZipFile zipFile;
		try
		{
			zipFile = new ZipFile(imageFolder+name+".zip");
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			File json = new File(imageFolder+"json.json");
			
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        InputStream stream = zipFile.getInputStream(entry);
		        
		        BufferedInputStream is = new BufferedInputStream(stream);
                int currentByte;
                byte data[] = new byte[4096];

                FileOutputStream fos = new FileOutputStream(json);
                BufferedOutputStream dest = new BufferedOutputStream(fos,4096);

                while ((currentByte = is.read(data, 0, 4096)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
                stream.close();		
		    }
		    zipFile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		//Renommer image.zip en image.bmp
		zipFile1.renameTo(imgFile);
	}
	
	public static byte[] convert(String string)
	{
		return Base64.getDecoder().decode(string);
	}
	
	public static String convert(SecretKey key)// byte[] encryptedBytes)
	{
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	private SecretKey encryptionPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		char[] pswd = password.toCharArray();
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3,
				(byte) 0x03 };
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(pswd, salt, 65536, 128);
		this.passwordKey = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(passwordKey.getEncoded(), "AES");
		return secret;
	}
	

	private byte[] decrypt(byte[] inpBytes, PrivateKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(inpBytes);
	}
	
	/**
	 * convertStringToSecretKey convertit le String key en SecretKey
	 * @param key
	 * String key a convertir
	 */
	private SecretKey convertStringToSecretKey(String key) {
		byte[] encoded = Base64.getDecoder().decode(key);
		SecretKey aesKey = new SecretKeySpec(encoded, "AES");
		return aesKey;
		/* autre version a tester
		byte[] encoded = key.getBytes();
		SecretKey aesKey = new SecretKeySpec(encoded, "AES");
		return aesKey;
		*/
	}
	
	/**
	 * encryptionPassword genere une clef avec l'algorithme xxxxx en utilisant
	 * le password
	 * 
	 * @param password
	 *            String a crypter avec l'algorithme xxxxxx et la clef aesKey
	 * @param publicKey
	 *            SecretKey pour crypter le password avec l'algorithme xxxx
	 * @return SecretKey secretKey correspondant au password crypter avec
	 *         l'algorithme xxxx et la clef publicKey
	 */
	private SecretKey encryptionPassword(String password, SecretKey publicKey) {
		// TODO
		/*
		byte[] salt ={ (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		publicKey = factory.generateSecret(spec);
		*/
		SecretKey secretKey = new SecretKeySpec(publicKey.getEncoded(), "AES");
		return secretKey;
	}
	
	/*
	 	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(512); // 512 is the keysize.
		KeyPair kp = kpg.generateKeyPair();
		PublicKey pubk = kp.getPublic();
		PrivateKey prvk = kp.getPrivate();
		byte[] dataBytes = "J2EE Security for Servlets, EJBs and Web Services".getBytes();
	 */
	/*
	public byte[] encrypt(String password, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(password.getBytes());
	}
	
	public byte[] decrypt(byte[] password, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(password);
	}
	*/
	/**
	 * convertStrinToBytes convertit un String en tableau de bytes
	 * @param encryptedString
	 * String a convertir
	 * @return byte[] bytes correspondant au String converti
	 */
	private byte[] convertStringToBytes(String encryptedString) {
		return Base64.getDecoder().decode(encryptedString);
	}
	
	/**
	 * decryptionData decrypte un tableau de bytes avec l'algorithme AES en utilisant la clef secretKey et renvoie un String
	 * @param secretKey
	 * SecretKey la clef pour crypter / decrypter
	 * @param encryptedBytes
	 * byte[] le tableau de bytes a decrypter
	 * @return
	 * String res le String correspondant au tableau de bytes decrypte
	 */
	private String decryptionData(SecretKey secretKey, byte[] encryptedBytes) {
		String res = null;
		try {
			// Encrypt cipher
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
			// Decrypt cipher
			Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			// Decrypt
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
			CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
				outputStream.write(buf, 0, bytesRead);
			}
			cipherInputStream.close();
			res = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	/**
	 * convertStringToInts convertit le String decrypte en matrice de int. Lorsque le String contient un "/" on recupere le pixel dans l'image
	 * @param decryptedString
	 * String a convertir
	 * @return
	 * int[][] res la matrice d'int 
	 */
	private int[][] convertStringToInts(String decryptedString) {
		int [][] res = null;
		BufferedImage image = model.getImage();
		for (int i = 0; i < image.getWidth(); i++)
		{
			for (int j = 0; j < image.getHeight(); j++)
			{
				boolean isIn = false;
				for (Rectangle r : rectangles)
				{
					if (r.contains(new Point(i, j)))
					{
						isIn = true;
						break;
					}
				}
				if (isIn)
				{
					// mettre les pixels random ???????????????????????
					//res += String.valueOf(image.getRGB(i, j));
				}
				//else
					//res += "/";
			}
		}
		return res;
	}

	/**
	 * createBufferedImage genere une image a partir d'un tableau de pixels et des dimensions de l'image
	 * @param rgbs
	 * int[][] le tableau de pixels de l'image
	 * @param width
	 * int la largeur de l'image
	 * @param height
	 * int la hauteur de l'image
	 * @return
	 * BufferedImage buff est l'image reconstituee avec les param�tres d'entree
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
	 * saveImage sauve l'image dans du param�tre bufferedImage
	 * @param bufferedImage
	 * BufferedImage bufferedImage l'image a sauve
	 */
	private void saveImage(BufferedImage bufferedImage) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * deleteJSON efface le json de l'image
	 * @param filePath
	 * String filePath correspond au chemin du fichier .json a supprimer
	 * @throws IOException 
	 */
	private void deleteJSON(String filePath) throws IOException {
		Files.deleteIfExists(Paths.get(filePath));
		
	}
}