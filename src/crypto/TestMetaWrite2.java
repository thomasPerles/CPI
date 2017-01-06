package crypto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestMetaWrite2
{
	public void hideZipInImage(String imageFolder, String imageName) throws IOException{
		//Creer un zip avec le json dedans
		FileInputStream in = new FileInputStream(imageFolder+imageName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(imageFolder+"json.zip"));
        out.putNextEntry(new ZipEntry(imageName)); 
        byte[] b = new byte[1024];
        int count;
        while ((count = in.read(b)) > 0) {
            out.write(b, 0, count);
        }
        out.close();
        in.close();
        
        
        //File imageFile = new File(imageFolder+imageName);
        
		//QUEL OS ?
        	//Windows ?
        		//ligne de commande ("copy /b img.bmp+xx.zip imb.bmp")
        	//Linux ?
        		//ligne de commande("")
        	//MAC ?
        		//ligne de commande("")
        
    	String os = System.getProperty("os.name").toLowerCase();

		if ((os.indexOf("win") >= 0)) { //WINDOWS
			try {
				ProcessBuilder builder = new ProcessBuilder(
						"cmd.exe", "/c", "cd \""+imageFolder+"\" && copy /b "+imageName+"+json.zip "+imageName);
			        builder.redirectErrorStream(true);
			        Process p = builder.start();
			        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        String line;
			        while (true) {
			            line = r.readLine();
			            if (line == null) { break; }
			            //System.out.println(line);
			        }
			    } catch (Exception ex) {}
		} else if (os.indexOf("mac") >= 0) { // MAC OS
			
		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ) { //UNIX-LINUX
			try{
				String command ="cd "+imageFolder+" && cat "+imageName+" json.zip > "+imageName;
				Process p;
				String s;
				p = Runtime.getRuntime().exec(command);
	            BufferedReader br = new BufferedReader(
	                new InputStreamReader(p.getInputStream()));
	            while ((s = br.readLine()) != null)
	                System.out.println("line: " + s);
	            p.waitFor();
	            System.out.println ("exit: " + p.exitValue());
	            p.destroy();
				
				
			} catch (Exception ex) {}
		} else if (os.indexOf("sunos") >= 0) { //SOLARIS
			
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
		//test.hideZipInImage("C:/Users/Flav/git/CPI/res-test/","TEST.bmp");
		test.hideZipInImage("res-test/","TEST.bmp");
		//test.findZipInImage("");
	}
	
	
}
