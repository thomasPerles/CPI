package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageModel {

	private BufferedImage image;
	private WritableRaster wRaster;
	private DataBuffer data;

	public ImageModel() {
		//image = new BufferedImage(344, 455, BufferedImage.TYPE_INT_RGB);
	}

	public ImageModel(String path) {
		loadByPixel(path);
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
			//ImageIO.write((RenderedImage) image, extension, new File(file.split("\\."+extension)[0]+"test."+extension));
			ImageIO.write(image, extension.toUpperCase(), new File(file));//.split("\\."+extension)[0]+"1"+"."+extension));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cree un BufferedImage a partir de l'image passee en parametre
	 * @param fileName
	 * String : image a importer
	 */
	public void loadByPixel(String fileName) {
		File file = new File(fileName);
		try {

			BufferedImage dummy = ImageIO.read(file);
			int width = dummy.getWidth();
			int height = dummy.getHeight();
/*
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					bi.setRGB(i, j, dummy.getRGB(i, j));
*/
			//setImage(bi);
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
				/* Create random color for pixel */
				//Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
				int alpha = 255;
				int red = rand.nextInt(255);
				int green = rand.nextInt(255);
				int blue = rand.nextInt(255);
				int p = (alpha << 24) | (red << 16) | (green << 8) | blue;
				
				/* Change pixel(i, j) color */
				//image.setRGB(i, j, color.getRGB());
				image.setRGB(i, j, p);
			}
	}
	/**
	 * Affiche le contenu de l'image
	 */
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("\nImage properties:\n");
		res.append(image);
		res.append('\n');

		/*
		 * res.append("\nRaster properties:\n"); res.append(wRaster);
		 * 
		 * res.append("\nData:\n"); res.append(offsetToString());
		 */

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

	public DataBuffer getImageRasterDataBuffer() {
		return data;
	}

	public WritableRaster getImageRaster() {
		return wRaster;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		this.wRaster = (WritableRaster) image.getData();
		this.data = image.getData().getDataBuffer();
	}
	
	
	
	public void rebuildImage(String string)
	{
		String[] pixels = string.split("/");

		
		for(int i = 0; i < pixels.length; i++)
		{
			int width = i / image.getHeight();
			
			// System.out.println("height : " + height);
			// System.out.println("x : " + i % image.getWidth());
			
			if(!pixels[i].equals("0"))
			{
				image.setRGB(width, i % image.getHeight(), Integer.parseInt(pixels[i]));
			}
				
		}
		
	}
}
