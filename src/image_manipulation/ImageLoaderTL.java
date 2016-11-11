package image_manipulation;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoaderTL extends ImageLoader {
	
	private BufferedImage image;
	private WritableRaster wRaster;
	private DataBuffer data;
	
	public static void main(String[] args) {
		ImageLoaderTL iTL = new ImageLoaderTL();
		try {
			iTL.loadImage("res-test/bearbull.bmp");
			System.out.println("Image loaded");
			System.out.println(iTL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("\nImage properties:\n");
		res.append(image);
		res.append("\nRaster properties:\n");
		res.append(wRaster);
		res.append("\nData:\n");
		res.append(data.getOffsets());
		/*
		for(int i = 0; i < data.getNumBanks(); i++) {
			res.append('\n');
			res.append('[');
			for(int j = 0; i < data.getSize(); i++) {
				res.append('\t').append(data.getElem(i, j)).append('\t');
			}
			res.append(']');
		}
		*/
		return res.toString();
	}
	
	public void loadImage(String filePath) throws IOException {
	    setImage(ImageIO.read(new File(filePath)));
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
		
	}
	
}
