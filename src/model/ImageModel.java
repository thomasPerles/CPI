package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageModel {

	private BufferedImage image;

	public ImageModel() {
	}
	
	/**
	 * constructeur de la classe imageModel avec le String path 
	 * @param path
	 * String representant le path du fichier
	 */
	public ImageModel(String path) {
		loadImage(path);
	}
	
	/**
	 * Enregistre l'image
	 * @param file
	 * String : fichier a sauver (dont extension)
	 * @param extension
	 * String : extension du fichier (ex. bmp, png, ...) 
	 */
	public void saveIMG(String file, String extension) {
		try {
			ImageIO.write(image, extension.toUpperCase(), new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cree un BufferedImage a partir de l'image passee en parametre
	 * @param fileName
	 * String : image a importer
	 */
	public void loadImage(String fileName) {
		File file = new File(fileName);
		try {

			BufferedImage dummy = ImageIO.read(file);
			int width = dummy.getWidth();
			int height = dummy.getHeight();
			setImage(dummy);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Pixelise le rectangle indique dans l'image
	 * @param x 
	 * int : coordonee X du rectangle 
	 * @param width
	 * int : largeur du rectangle
	 * @param y
	 * int : coordonee Y du rectangle 
	 * @param height
	 * int : hauteur du rectangle
	 */
	public void pixelateImage(int x, int width, int y, int height) {
		Random rand = new Random();
		for (int i = x; i < width + x; i++)
			for (int j = y; j < height + y; j++) {
				/* Create random color for pixel */
				Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				/* Change pixel(i, j) color */
				image.setRGB(i, j, color.getRGB());
			}
	}
	
	/**
	 * Pixelise le rectangle indique dans l'image
	 * @param rectangle
	 * Rectangle : rectangle a pixeliser
	 */
	public void pixelateImage(Rectangle rectangle) {
		Random rand = new Random();
		for (int i = rectangle.x; i < rectangle.width + rectangle.x; i++)
			for (int j = rectangle.y; j < rectangle.height + rectangle.y; j++) {
				int alpha = 255;
				int red = rand.nextInt(255);
				int green = rand.nextInt(255);
				int blue = rand.nextInt(255);
				int p = (alpha << 24) | (red << 16) | (green << 8) | blue;
				image.setRGB(i, j, p);
			}
	}
	
	/**
	 * Affiche le contenu de l'image
	 * @return
	 * String le contenu de l'image
	 */
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("\nImage properties:\n");
		res.append(image);
		res.append('\n');

		for (int i = 0; i < image.getHeight(); i++) {
			res.append('[');
			for (int j = 0; j < image.getWidth(); j++) {
				res.append('\t').append(image.getRGB(j, i));
			}
			res.append('\t').append(']');
			res.append('\n');
		}
		return res.toString();
	}
	
	/**
	 * getImage renvoie l'image
	 * @return
	 * BufferedImage l'image renvoye
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * setImage modifie l'image
	 * @param image
	 * BufferedImage l'image utilisee
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * rebuildImage reconstitue l'image originale a partir du contenu dechiffre 
	 * @param string
	 * String le contenu dechiffre a reconstituer
	 */
	public void rebuildImage(String string)
	{
		String[] pixels = string.split("/");
		for(int i = 0; i < pixels.length; i++)
		{
			int width = i / image.getHeight();
			if(!pixels[i].equals("0"))
			{
				image.setRGB(width, i % image.getHeight(), Integer.parseInt(pixels[i]));
			}
		}
	}
}