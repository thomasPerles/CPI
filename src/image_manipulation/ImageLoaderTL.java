package image_manipulation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageLoaderTL extends ImageLoader {

    private BufferedImage image;
    private WritableRaster wRaster;
    private DataBuffer data;
    private ImageDisplay id;
    
    public static void main(String[] args) {
	ImageLoaderTL iTL = new ImageLoaderTL();
	
	iTL.loadByPixel("res-test/bearbull.bmp");
	
	iTL.pixelateImage(50, 60, 10, 200);
	iTL.pixelateImage(150, 60, 10, 150);
	
	// System.out.println("Image loaded");
	// System.out.println(iTL);
	
	// iTL.pixelateImage(10, 60, 10, 200);
	
	iTL.getImageDisplay().run();
    }
    
    public void saveBMP() {
	try {
	    ImageIO.write((RenderedImage)image, "bmp", new File("res-test/test.bmp"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void loadByPixel(String fileName) {
	File file = new File(fileName);
	try {
	    
	    BufferedImage dummy = ImageIO.read(file);
	    int width = dummy.getWidth();
	    int height = dummy.getHeight();
	    
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    for(int i = 0; i < width; i++)
		for(int j = 0; j < height; j++)
		    bi.setRGB(i, j, dummy.getRGB(i, j));
	    
	    setImage(bi);
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }

    public void pixelateImage(int x, int width, int y, int height){
	Random rand = new Random();	
	for(int i = x; i < width + x; i++)
	    for(int j = y; j < height + y; j++) {
		/* Create random color for pixel */
		Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		/* Change pixel(i, j) color */
		image.setRGB(i, j, color.getRGB());
	    }
    }

    public String toString() {
	StringBuilder res = new StringBuilder();
	res.append("\nImage properties:\n");
	res.append(image);
	res.append('\n');
	
	/*
	res.append("\nRaster properties:\n");
	res.append(wRaster);
	
	res.append("\nData:\n");
	res.append(offsetToString());
	*/
	
	for(int i = 0; i < image.getHeight(); i++) {
	    res.append('[');
	    for(int j = 0; j < image.getWidth(); j++) {
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

    public ImageLoaderTL() {
	this.id = new ImageDisplay(this);
    }
    
    public String offsetToString() {
	StringBuilder res = new StringBuilder('[');
	for (int i = 0; i < data.getNumBanks(); i++) {
	    res.append(' ').append(data.getOffsets()[i]).append(' ');
	}
	res.append(' ');
	return res.toString();
    }
    
    public ImageDisplay getImageDisplay(){
	return this.id;
    }

}
