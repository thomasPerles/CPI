package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;

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
				System.out.println("d�but crypto");
				
				
				if (passwordTextField.getText() != null)
					password = passwordTextField.getText();


				// Cr�er le fichier json
				//if (Main.model != null) {
				if (model != null) {
					//imageModel = Main.model;

					// String filename = imageModel.getImage().;
					BufferedImage image = model.getImage();

					// TODO
					// pixels selectionnes dans la matrice
					// pixels en RGB originaux
					int[][] rgbs = getRGB(image);

					// concatene les pixels rgb
					String rgbString = rgbtoString(rgbs, image.getWidth(), image.getHeight());
					System.out.println(rgbString);
					
					// avec RSA, mdp -> cle session KEYGENERATOR ou SECRETKEY ?
					SecretKey aesKey = null;
					try {
						aesKey = encryptionPassword(password);
					} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e3) {
						e3.printStackTrace();
					}

					// avec AES et cle de session, crypte les donnees de limage
					// (=vecteur rgbs)
					byte[] encryptedBytes = encryptionData(aesKey, rgbs, image);

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

					// TODO
					/*
					 * CRYPTAGE recuperer le mdp et limage avec RSA, mdp -> cle
					 * session donnees de limage dans vecteur rgb avec AES et
					 * cle de session, crypte les donnees de limage stocker le
					 * vecteur et la cle de session dans le json si vecteur
					 * crypte incompatible avec les donnees de limage, donnees
					 * de limage deviennent random ... sauvegarder limage
					 * cryptee
					 * 
					 * DECRYPTAGE recuperer le mdp et limage avec RSA, verifier
					 * que le mdp = cle de session avec AES et cle de session,
					 * decrypter le vecteur rgb dans json ou dans les donnees de
					 * limage ... sauvegarder limage et supprimer le json
					 */

					// recr�e l'image avec les RGB originaux
					File outputfile = new File("saved.jpg");
					try {
						ImageIO.write(createBufferedImage(rgbs, image.getWidth(), image.getHeight()), "jpg",
								outputfile);
					} catch (IOException e2) {
						e2.printStackTrace();
					}

					try {
						// Getting filePath and fileName from MainWindow
						//imageModelJSON.writeImageModelJSONFile(Main.filePath, Main.fileName, password, encryptedString);
						imageModelJSON.writeImageModelJSONFile(path, fileName, password, encryptedString);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
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
	/*
	 * protected void encryption(int[][] rgbs, BufferedImage image) { String s =
	 * rgbtoString(rgbs, image.getWidth(), image.getHeight());
	 * //System.out.println(s); try { // Generate key KeyGenerator kgen =
	 * KeyGenerator.getInstance("AES"); kgen.init(128); SecretKey aesKey =
	 * kgen.generateKey(); System.out.println(aesKey.toString()); // Encrypt
	 * cipher Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	 * encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
	 * 
	 * // Encrypt ByteArrayOutputStream outputStream = new
	 * ByteArrayOutputStream(); CipherOutputStream cipherOutputStream = new
	 * CipherOutputStream(outputStream, encryptCipher);
	 * cipherOutputStream.write(s.getBytes()); cipherOutputStream.flush();
	 * cipherOutputStream.close(); byte[] encryptedBytes =
	 * outputStream.toByteArray(); System.out.println(encryptedBytes); //
	 * Decrypt cipher Cipher decryptCipher =
	 * Cipher.getInstance("AES/CBC/PKCS5Padding"); IvParameterSpec
	 * ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
	 * decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
	 * System.out.println(ivParameterSpec.toString()); // Decrypt outputStream =
	 * new ByteArrayOutputStream(); ByteArrayInputStream inStream = new
	 * ByteArrayInputStream(encryptedBytes); CipherInputStream cipherInputStream
	 * = new CipherInputStream(inStream, decryptCipher); byte[] buf = new
	 * byte[1024]; int bytesRead; while ((bytesRead =
	 * cipherInputStream.read(buf)) >= 0) { outputStream.write(buf, 0,
	 * bytesRead); }
	 * 
	 * System.out.println("Result: " + new String(outputStream.toByteArray()));
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } }
	 */

	private SecretKey encryptionPassword(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		byte[] salt = new byte[] { 0x7d, 0x60, 0x43, 0x5f, 0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0xae };
	    int iterationCount = 2048;
		PBEParameterSpec pSpecs = new PBEParameterSpec(salt, iterationCount);
		PBEKeySpec kSpecs = new PBEKeySpec(password.toCharArray());
		SecretKey key = keyFact.generateSecret(kSpecs);
		return key;
	}

	// TODO
	// est-ce necessaire ???????????????????????????????????????????
	private String convert(byte[] encryptedBytes) {
		String s = "";
		for (int i = 0; i < encryptedBytes.length; i++) {
			s += encryptedBytes[i];
		}
		return s;
	}

	private byte[] encryptionData(SecretKey aesKey, int[][] rgbs, BufferedImage image) {
		String s = rgbtoString(rgbs, image.getWidth(), image.getHeight());
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

	public String rgbtoString(int[][] rgbs, int width, int height) {
		String res = "";
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				res += String.valueOf(rgbs[i][j]);
			}
		}
		return res;
	}

	public static int[][] getRGB(BufferedImage image) {
		// System.out.println(image.getHeight() + " " + image.getWidth());
		// System.out.println("rgb 1, 1 = " + image.getRGB(1, 1));
		int[][] rgbs = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				// System.out.println("i = " + i + " j = " + j + " RGB = " +
				// image.getRGB(i, j));
				rgbs[i][j] = image.getRGB(i, j);
			}
		}
		return rgbs;
	}

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
