package crypto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public class TestMetaWrite2
{
	public void hideZipInImage(String imageFolder, String imageName) throws IOException{
		//Creer un zip avec le json dedans
		FileInputStream in = new FileInputStream(imageFolder+imageName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(imageFolder+"tmp.zip"));
        out.putNextEntry(new ZipEntry(imageName)); 
        byte[] b = new byte[1024];
        int count;
        while ((count = in.read(b)) > 0) {
            out.write(b, 0, count);
        }
        out.close();
        in.close();
        
		//QUEL OS ?
        	//Windows ?
        		//ligne de commande ("copy /b img.bmp+xx.zip imb.bmp")
        	//Linux ?
        		//ligne de commande("")
        	//MAC ?
        		//ligne de commande("")
        
    	String os = System.getProperty("os.name").toLowerCase();

		System.out.println(os);

		if ((os.indexOf("win") >= 0)) {
			System.out.println("This is Windows");
		} else if (os.indexOf("mac") >= 0) {
			System.out.println("This is Mac");
		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ) {
			System.out.println("This is Unix or Linux");
		} else if (os.indexOf("sunos") >= 0) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
	}
	
	public void findZipInImage(String imagePath){
		//renommer image.bmp en image.zip
		//http://stackoverflow.com/questions/12209801/how-to-change-file-extension-at-runtime
		
		
		//récupérer le contenu du zip (un json) et le copier à part
		//http://stackoverflow.com/questions/15667125/read-content-from-files-which-are-inside-zip-file
		
		//renommer image.zip en image.bmp
		//http://stackoverflow.com/questions/12209801/how-to-change-file-extension-at-runtime
	}
	
	
	
	public static void main(String[] args) throws IOException{
		//MAIN DE TEST !!
		TestMetaWrite2 test = new TestMetaWrite2();
		test.hideZipInImage("C:/Users/Flav/git/CPI/res-test/","bearbull.bmp");
		//test.findZipInImage("");
	}
	
	
}
