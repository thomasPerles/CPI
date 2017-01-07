package crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TestMetaWrite2
{
	
	/**
	 * Cette fonction sert a cacher un fichier json dans un zip, lui-meme cache dans une image.	<br>
	 * ATTENTION : Supprime le zip ainsi que le fichier json !
	 * @param imageFolder
	 * Contient le path vers le dosier contenant l'image
	 * @param imageName
	 * Contient le nom de l'image (extension incluse)
	 * @param jsonFolder
	 * Contient le path vers le dosier contenant le json
	 * @param jsonName
	 * Contient le nom du fichier json (extension .json incluse)
	 * @throws IOException 
	 */
	public void hideZipInImage(String imageFolder, String imageName, String jsonFolder, String jsonName) throws IOException
		{
		// Creer un zip avec le json dedans
		File imageFile = new File(imageFolder + imageName);
		File jsonFile = new File(jsonFolder + jsonName);
		imageFolder = imageFile.getAbsolutePath().split(imageName)[0];
		jsonFolder = jsonFile.getAbsolutePath().split(jsonName)[0];

		FileInputStream in = new FileInputStream(jsonFolder + jsonName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(imageFolder + "json.zip"));
		out.putNextEntry(new ZipEntry(jsonName));
		byte[] b = new byte[1024];
		int count;
		while ((count = in.read(b)) > 0)
		{
			out.write(b, 0, count);
		}
		out.close();
		in.close();


		String os = System.getProperty("os.name").toLowerCase();//OS detection

		if ((os.indexOf("win") >= 0))
		{ // WINDOWS
			try
			{
				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
						"cd \"" + imageFolder + "\" && copy /b " + imageName + "+json.zip " + imageName);
				builder.redirectErrorStream(true);
				Process p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while (true)
				{
					line = r.readLine();
					if (line == null)
					{
						break;
					}
					// System.out.println(line);
				}
			}
			catch (Exception ex)
			{
			}
		}
		else if (os.indexOf("mac") >= 0)
		{ // MAC OS

		}
		else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0)
		{ // UNIX-LINUX
			try
			{	
				String command = "cd " + imageFolder + " && cat " + imageName + " json.zip > temp && cat temp > " + imageName + " && rm temp";
				System.out.println(command);
				String s;
		        Process p;
		        try {
		        	p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
		            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            while ((s = br.readLine()) != null)
		                //System.out.println("line: " + s); //Sortie de la commande
		            p.waitFor();
		            //System.out.println ("exit: " + p.exitValue());//Code de sortie de la commande
		            p.destroy();
		        } catch (Exception e) {}
			}catch (Exception ex){}
		}
		else
		{
			System.out.println("Your OS is not supported!!");
		}
		
		//Supprimer le zip		
		try {
			Files.deleteIfExists(Paths.get(imageFolder+"json.zip"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Supprimer le json d'origine !
		try {
			Files.deleteIfExists(Paths.get(imageFolder+jsonName));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static void main(String[] args) throws IOException
	{
		// MAIN DE TEST !!
		TestMetaWrite2 test = new TestMetaWrite2();
		test.hideZipInImage("res-test/test/", "TEST.bmp","res-test/test/","oneTexte.json");
		test.findZipInImage("res-test/test/", "TEST.bmp");
	}

}
