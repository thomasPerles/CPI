package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ImageModelJSON {
	/**
	 * Permet de lire le contenu d'un fichier .json dont le nom est le parametre
	 * file_Name et d'en renvoyer le contenu
	 * 
	 * @param file_Path
	 *            String qui correspond au path du fichier .json
	 * @param file_Name
	 *            String qui correspond au nom du fichier .json
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public String[] readImageFromJson(String file_Path, String file_Name)
			throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		FileReader fr = new FileReader(file_Path);
		Object obj = parser.parse(fr);
		JSONObject jsonObject = (JSONObject) obj;

		String filePath = jsonObject.get("filePath").toString();
		String fileName = jsonObject.get("fileName").toString();
		String encryptedString = jsonObject.get("encryptedString").toString();
		String publicKey = jsonObject.get("clef publique").toString();
		String privateKey = jsonObject.get("clef privee").toString();
		String sessionKey = jsonObject.get("clef session").toString();

		String[] res = { filePath, fileName, encryptedString, publicKey, privateKey, sessionKey };
		fr.close();
		return res;
	}


	/**
	 * Cree un fichier .json contenant le chemin du fichier, le nom du fichier,
	 * la clef et le String correspondant au tableau de Bytes crpte
	 * 
	 * @param filePath
	 *            String correspondant au chemin du fichier
	 * @param fileName
	 *            String correspondant au nom du fichier
	 * @param encryptedString
	 *            String correspondant au tableau de Bytes cryptes
 	 * @param publicKey
	 *            String correspondant a la clef publique pour l'algorithme de cryptage
	 * @param privateKey
	 *            String correspondant a la clef privee pour l'algorithme de cryptage
	 * @param sessionKey
	 *            String correspondant a la clef de session pour l'algorithme de cryptage
	 * @return String[] contenant le path du dossier du fichier json ainsi que
	 *         le nom du fichier json
	 * @throws IOException
	 */
	public String[] writeImageModelJSONFile(String filePath, String fileName, String encryptedString, String publicKey,
			String privateKey, String sessionKey) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("filePath", filePath);
		obj.put("fileName", fileName);
		;
		obj.put("encryptedString", encryptedString);
		obj.put("clef publique", publicKey);
		obj.put("clef privee", privateKey);
		obj.put("clef session", sessionKey);
		String folder = filePath.split(fileName)[0];
		String fileStr = fileName.split("\\.")[0] + "_" + fileName.split("\\.")[1] + ".json";
		String totalFile = folder + fileStr;
		try (FileWriter file = new FileWriter(totalFile)) {
			file.write(obj.toJSONString());
		}

		String[] res = { folder, fileStr };
		return res;
	}

}