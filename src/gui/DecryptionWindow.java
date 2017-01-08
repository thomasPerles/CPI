package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
					if(test.equals(test2))
					{
						String initVector = "RandomInitVector";
						String result = newDecrypt(password, initVector, encryptedString, passwordKey);
						model.rebuildImage(result);
						view.repaint();
						model.saveIMG(path, fileName.split("\\.")[1]);
					}
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
					e2.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					deleteJSON(folder+"json.json");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				frame.setVisible(false);
				frame.dispose();
				model.loadImage(path);
				view.repaint();
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
				buff.setRGB(i, j, rgbs[i][j]);
			}
		}
		return buff;
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