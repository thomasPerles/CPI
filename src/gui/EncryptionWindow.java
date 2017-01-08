package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
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
	
	/**
	 * setVisible affiche ou cache la fenetre frame
	 * @param state
	 * boolean : true permet d'afficher la fenetre frame et false la cache 
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
				System.out.println("début crypto");

				if (passwordTextField.getText() != null)
					password = passwordTextField.getText();
				
				if (model != null) {
					BufferedImage image = model.getImage();
					
					// le String correspondant aux donnees a crypter 
					String rgbString = getRGBToString(image);
					System.out.println(rgbString);

					// avec RSA, mdp -> cle session KEYGENERATOR ou SECRETKEY ?
					SecretKey aesKey = null;
					try {
						aesKey = encryptionPassword(password);
					} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
							| InvalidKeyException | InvalidAlgorithmParameterException e3) {
						e3.printStackTrace();
					}

					// avec AES et cle de session, crypte les donnees de limage
					// (=vecteur rgbs)
					byte[] encryptedBytes = encryptionData(aesKey, rgbString);

					// stocker le vecteur et la cle de session dans le json
					String encryptedString = convert(encryptedBytes);

					// si vecteur crypte incompatible avec les donnees de
					// limage, donnees de limage deviennent random
					// TODO

					// ...
					// sauvegarder limage cryptee

					// * DECRYPTAGE
					// recuperer le mdp et limage
					// avec RSA, verifier que le mdp = cle de session
					// avec AES et cle de session, decrypter le vecteur rgb dans
					// json ou dans les donnees de limage
					// ...
					// sauvegarder limage et supprimer le json

					

					// recrée l'image avec les RGB originaux
					/*File outputfile = new File("saved.jpg");
					try {
						ImageIO.write(createBufferedImage(rgbs, image.getWidth(), image.getHeight()), "jpg",
								outputfile);
					} catch (IOException e2) {
						e2.printStackTrace();
					}*/

					try {
						// Getting filePath and fileName from MainWindow
						// imageModelJSON.writeImageModelJSONFile(Main.filePath,
						// Main.fileName, password, encryptedString);
						imageModelJSON.writeImageModelJSONFile(path, fileName, convert(aesKey.getEncoded()), encryptedString);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					//TODO : CACHER LE JSON !!!!!
				}

				// TODO

				// Recuperer les rectangles de la vue
				rectangles = view.getRectangles();

				// Recuperer un emplacement commun a plusieurs os et l'utiliser
				// pour le stockage de donnees
				String location = System.getProperty("user.home");
				File file = new File(location, "Test.txt");

				try {
					// Ecrire des octets dans un fichier
					// FileOutputStream fout = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(file);

					pw.write("test");
					pw.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				// Gestion du mot de passe
				/*
				 * if (passwordTextField.getText().equals("password"))
				 * frame.setContentPane(new ResultPanel(frame,
				 * "Encryption successed")); else frame.setContentPane(new
				 * ResultPanel(frame, "Encryption failed")); frame.revalidate();
				 */
			}

		});
	}
	
	// TODO
	/**
	 * encryptionPassword genere une clef avec l'algorithme xxxxx en utilisant le password
	 * @param password
	 * String qui est crypte avec l'algorithme xxxx
	 * @return
	 * SecretKey aesKey la clef genere par l'algorithme xxxx depuis le password
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	private SecretKey encryptionPassword(String password) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		SecretKey aesKey = kgen.generateKey();
		return aesKey;
		/*
		SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		byte[] salt = {(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,(byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03};
		int iterationCount = 2048;
		PBEParameterSpec pSpecs = new PBEParameterSpec(salt, iterationCount);
		PBEKeySpec kSpecs = new PBEKeySpec(password.toCharArray());
		SecretKey key = keyFact.generateSecret(kSpecs);
		cipher.init(Cipher.ENCRYPT_MODE, key, pSpecs);
		return key;
		*/
	}

	// TODO
	// est-ce necessaire ???????????????????????????????????????????
	/**
	 * convert convertit le tableau de Bytes en String
	 * @param encryptedBytes
	 * bytes[] le tableau de Bytes a convertir
	 * @return
	 * String s la conversion du tableau de Bytes
	 */
	private String convert(byte[] encryptedBytes) {
		String s = "";
		for (int i = 0; i < encryptedBytes.length; i++) {
			s += encryptedBytes[i];
		}
		return s;
	}

	/**
	 * encryptionData crypte le rgbString avec l'algorithme AES en utilisant la clef aesKey 
	 * @param aesKey
	 * SecretKey la clef utilisee lors de l'algorithme AES pour crypter rgbString
	 * @param rgbString
	 * String qui est crypte avec l'algorithme AES et aesKey
	 * @return
	 * byte[] encryptedBytes qui est le tableau de Bytes correspondant a rgbString crypte
	 */
	private byte[] encryptionData(SecretKey aesKey, String rgbString) {
		String s = rgbString;//rgbtoString(rgbString, image.getWidth(), image.getHeight());
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
	 * getRGBToString genere le String a crypter : "/" pour les pixels a ne pas crypter et la valeur en Bytes pour les pixels a crypter
	 * @param image
	 * BufferedImage l'image a crypter
	 * @return 
	 * String res correspondant aux donnees de l'image a crypter
	 */
	public String getRGBToString(BufferedImage image) {
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
					// mettre les pixels random   ???????????????????????
					res += String.valueOf(image.getRGB(i, j)); 
				}
				else res += "/";
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
	 * BufferedImage buff est l'image reconstituee avec les paramètres d'entree
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
}