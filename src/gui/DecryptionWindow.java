package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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
				// recuperer le mdp et limage
				if (passwordTextField.getText() != null)
					password = passwordTextField.getText();

				ImageModel imageModel = null;

				// recuperer les valeurs dans le json
				String[] jsonString = null;
				try {
					jsonString = imageModelJSON.readImageFromJson(fileName);
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				String filePath = jsonString[0];
				String fileName = jsonString[1];
				String key = jsonString[2];
				String encryptedString = jsonString[3];
				System.out.println("filePath : " + filePath + "\nfileName : " + fileName + "\nkey : " + key + "\nencryptedString : " + encryptedString);

				// avec RSA, verifier que le mdp = cle de session
				SecretKey secretKey = convertStringToSecretKey(key);
				SecretKey aesKey = encryptionPassword(password, secretKey);

				// avec AES et cle de session, decrypter le vecteur rgb dans json ou dans les donnees de limage
				byte[] decryptedBytes = decryptionData(aesKey, imageModel.getImage(), encryptedString);
				
				// convertir vers int rgb[][];
				int rgbs[][] = convertBytesToInts(decryptedBytes);
				
				// sauvegarder limage et supprimer le json
				BufferedImage bufferedImage = createBufferedImage(rgbs, imageModel.getImage().getWidth(), imageModel.getImage().getHeight());
				saveImage(bufferedImage);
				deleteJSON(filePath);

				/*
				 * // Gestion du mot de passe if
				 * (passwordTextField.getText().equals("password"))
				 * frame.setContentPane(new ResultPanel(frame,
				 * "Decryption successed")); else frame.setContentPane(new
				 * ResultPanel(frame, "Decryption failed")); frame.revalidate();
				 */
			}
		});

	}
	
	/**
	 * saveImage sauve l'image dans du paramètre bufferedImage
	 * @param bufferedImage
	 * BufferedImage bufferedImage l'image a sauve
	 */
	private void saveImage(BufferedImage bufferedImage) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * convertBytesToInts convertit un tableau de byte en matrice de int
	 * @param decryptedBytes
	 * byte[] le tabeau de bytes a convertir
	 * @return
	 * int[][] res la matrice d'int 
	 */
	private int[][] convertBytesToInts(byte[] decryptedBytes) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * deleteJSON efface le json de l'image
	 * @param filePath
	 * String filePath correspond au chemin du fichier .json a supprimer
	 */
	private void deleteJSON(String filePath) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * convertStringToSecretKey convertit le String key en SecretKey
	 * * @param key
	 * String key a convertir
	 */
	private SecretKey convertStringToSecretKey(String key) {
		byte[] encoded = key.getBytes();
		SecretKey aesKey = new SecretKeySpec(encoded, "AES");
		return aesKey;
	}

	/**
	 * encryptionPassword genere une clef avec l'algorithme xxxxx en utilisant
	 * le password
	 * 
	 * @param password
	 *            String a crypter avec l'algorithme xxxxxx et la clef aesKey
	 * @param aesKey
	 *            SecretKey pour crypter le password avec l'algorithme xxxx
	 * @return SecretKey aesKey correspondant au password crypter avec
	 *         l'algorithme xxxx et la clef aesKey
	 */
	private SecretKey encryptionPassword(String password, SecretKey aesKey) {
		// TODO Auto-generated method stub
		return aesKey;
	}

	private byte[] decryptionData(SecretKey aesKey, BufferedImage image, String encryptedString) {
		try {
			// Encrypt cipher
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
			// Encrypt
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
			cipherOutputStream.write(encryptedString.getBytes());
			cipherOutputStream.flush();
			cipherOutputStream.close();
			byte[] encryptedBytes = outputStream.toByteArray();
			// System.out.println(encryptedBytes);
			// Decrypt cipher
			Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(encryptCipher.getIV());
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
			// System.out.println(ivParameterSpec.toString());
			// Decrypt
			outputStream = new ByteArrayOutputStream();
			ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
			CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
				outputStream.write(buf, 0, bytesRead);
			}
			
			// System.out.println("Result: " + new String(outputStream.toByteArray()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private SecretKey decryptionPassword(String password, SecretKey tmp) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		//char[] pswd = password.toCharArray();
		//byte[] salt ={ (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };
		//SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		//KeySpec spec = new PBEKeySpec(pswd, salt, 65536, 128);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		return secret;
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