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
	 * readImageFromJson permet de lire le contenu d'un fichier .json dont le
	 * nom est le parametre file_Name et d'en renvoyer le contenu
	 * 
	 * @param file_Name
	 *            String qui correspond au nom du fichier .json
	 * @return String[] res qui renvoie le contenu du fichier .json : le chemin
	 *         du fichier, le nom du fichier, la clef et le String correspondant
	 *         au tableau de Bytes cryptes
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	/*
	 * public String[] readImageFromJson(String file_Name) throws
	 * FileNotFoundException, IOException, ParseException { String[] res = null;
	 * JSONParser parser = new JSONParser(); Object obj = parser.parse( new
	 * FileReader("res-test/" + file_Name.split("\\.")[0] + "_" +
	 * file_Name.split("\\.")[1] + ".json")); JSONObject jsonObject =
	 * (JSONObject) obj;
	 * 
	 * String filePath = jsonObject.get("filePath").toString(); res[res.length]
	 * = filePath; String fileName = jsonObject.get("fileName").toString();
	 * res[res.length] = fileName; String key =
	 * jsonObject.get("clef").toString(); res[res.length] = key; String
	 * encryptedString = jsonObject.get("encryptedString").toString();
	 * res[res.length] = encryptedString;
	 * 
	 * System.out.println("Successfully reading JSON Object...");
	 * System.out.println("\nJSON Object: " + obj); return res; }
	 */
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
		// String folder = file_Path.split(file_Name)[0];
		// String fileStr = folder + file_Name.split("\\.")[0] + "_" +
		// file_Name.split("\\.")[1] + ".json";
		FileReader fr = new FileReader(file_Path);
		Object obj = parser.parse(fr);
		JSONObject jsonObject = (JSONObject) obj;

		String filePath = jsonObject.get("filePath").toString();
		String fileName = jsonObject.get("fileName").toString();
		String encryptedString = jsonObject.get("encryptedString").toString();
		String publicKey = jsonObject.get("clef publique").toString();
		String privateKey = jsonObject.get("clef privee").toString();
		String sessionKey = jsonObject.get("clef session").toString();

		//System.out.println("Successfully reading JSON Object...");
		//System.out.println("\nJSON Object: " + obj);

		String[] res = { filePath, fileName, encryptedString, publicKey, privateKey, sessionKey };
		fr.close();
		return res;
	}

	/**
	 * writeImageModelJSONFile cree un fichier .json contenant le chemin du
	 * fichier, le nom du fichier, la clef et le Dtring correspondant au tableau
	 * de Bytes crptes
	 * 
	 * @param filePath
	 *            String correspondant au chemin du fichier
	 * @param fileName
	 *            String correspondant au nom du fichier
	 * @param key
	 *            String correspondant a la clef pour l'algorithme de cryptage
	 * @param encryptedString
	 *            String correspondant au tableau de Bytes cryptes
	 * @throws IOException
	 */
	/*
	 * public void writeImageModelJSONFile(String filePath, String fileName,
	 * String key, String encryptedString) throws IOException { JSONObject obj =
	 * new JSONObject(); obj.put("filePath", filePath); obj.put("fileName",
	 * fileName); obj.put("clef", key); obj.put("encryptedString",
	 * encryptedString);
	 * 
	 * // try-with-resources statement based on post comment below :) try
	 * (FileWriter file = new FileWriter( "res-test/" + fileName.split("\\.")[0]
	 * + "_" + fileName.split("\\.")[1] + ".json")) {
	 * file.write(obj.toJSONString()); System.out.println(
	 * "Successfully Copied JSON Object to File..."); System.out.println(
	 * "\nJSON Object: " + obj); } }
	 */

	/**
	 * Cree un fichier .json contenant le chemin du fichier, le nom du fichier,
	 * la clef et le String correspondant au tableau de Bytes crpte
	 * 
	 * @param filePath
	 *            String correspondant au chemin du fichier
	 * @param fileName
	 *            String correspondant au nom du fichier
	 * @param key
	 *            String correspondant a la clef pour l'algorithme de cryptage
	 * @param encryptedString
	 *            String correspondant au tableau de Bytes cryptes
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
		// try-with-resources statement based on post comment below :)
		String folder = filePath.split(fileName)[0];
		String fileStr = fileName.split("\\.")[0] + "_" + fileName.split("\\.")[1] + ".json";
		String totalFile = folder + fileStr;// fileName.split("\\.")[0] + "_" +
											// fileName.split("\\.")[1] +
											// ".json";
		try (FileWriter file = new FileWriter(totalFile)) {
			file.write(obj.toJSONString());
			//System.out.println("Successfully Copied JSON Object to File...");
			//System.out.println("\nJSON Object: " + obj);
		}

		String[] res = { folder, fileStr };
		return res;
	}

}