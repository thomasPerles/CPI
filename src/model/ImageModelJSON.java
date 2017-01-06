package model;

import org.json.simple.JSONObject;


public class ImageModelJSON {

	 public ImageModel readImageFromJson(String fileName) {
	        /*SCollection fileToCollection = new SCollection();
	        ArrayList<Shape> shapes = new ArrayList<>();
	        JSONParser parser = new JSONParser();
	        try {
	            Object obj = parser.parse(new FileReader(fileName));
	            JSONObject jsonObject = (JSONObject) obj;

	            JSONArray shapesArray = (JSONArray) jsonObject.get("shapes");
	            for (int i = 0; i < shapesArray.size(); i++) { // on itère sur le tableau de shapes
	                JSONObject shapeTmp = (JSONObject) shapesArray.get(i);
	                //la classe du shape
	                Shape shapeToSave = null;
	                switch (shapeTmp.get("class").toString()) {
		                case "Rectangle":
		                    shapeToSave = this.createRectangle(shapeTmp);
		                    break;
		                case "Cercle":
		                	shapeToSave = this.createCercle(shapeTmp);
		                	break;
		                case "Texte":
		                	shapeToSave = this.createTexte(shapeTmp);
		                	break;
		                case "Collection":
		                	shapeToSave = this.createCollection(shapeTmp);
		                	break;
		                case "Polygon":
		                	shapeToSave = this.createPolygon(shapeTmp);
		                	break;
		                case "PolygonRegulier":
		                	shapeToSave = this.createPolygonRegulier(shapeTmp);
		                	break;
	                }
	                shapes.add(shapeToSave);
	                fileToCollection.setShapesCollection(shapes);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return fileToCollection;*/
		 return null;
	 }

	public ImageModel createRectangle(JSONObject imageModel) {
		/*int width = Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("width").toString());
		int height = Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("height").toString());
		Point loc = new Point(Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("x").toString()), Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("y").toString()));
		
		boolean filled = Boolean.parseBoolean(((JSONObject)shapeTmp.get("color")).get("filled").toString());
		boolean stroked = Boolean.parseBoolean(((JSONObject)shapeTmp.get("color")).get("stroked").toString());
		String filledColorString = ((JSONObject)shapeTmp.get("color")).get("filledColor").toString();
		Color filledColor = Color.decode(filledColorString);
		String strokedColorString = ((JSONObject)shapeTmp.get("color")).get("strokedColor").toString();
		Color strokedColor = Color.decode(strokedColorString);
		
		ColorAttributes colorAttributes = new ColorAttributes(filled, stroked, filledColor, strokedColor);
		SRectangle rectangle = new SRectangle(loc, width, height);
		
		rectangle.addAttributes(colorAttributes);
		rectangle.addAttributes(new SelectionAttributes());
		
		return rectangle;*/
		return null;
	}
	
	/*public Shape createTexte(JSONObject shapeTmp) {
		Point loc = new Point(Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("x").toString()), Integer.parseInt(((JSONObject)shapeTmp.get("prop")).get("y").toString()));
		String text = ((JSONObject)shapeTmp.get("prop")).get("text").toString();
		
		boolean filled = Boolean.parseBoolean(((JSONObject)shapeTmp.get("color")).get("filled").toString());
		boolean stroked = Boolean.parseBoolean(((JSONObject)shapeTmp.get("color")).get("stroked").toString());
		String filledColorString = ((JSONObject)shapeTmp.get("color")).get("filledColor").toString();
		Color filledColor = Color.decode(filledColorString);
		String strokedColorString = ((JSONObject)shapeTmp.get("color")).get("strokedColor").toString();
		Color strokedColor = Color.decode(strokedColorString);
		
		String fontNameString = ((JSONObject)shapeTmp.get("font")).get("name").toString();
		String fontStyleString = ((JSONObject)shapeTmp.get("font")).get("style").toString();
		int fontSize = Integer.parseInt(((JSONObject)shapeTmp.get("font")).get("size").toString());
		Font font = Font.decode(fontNameString + "-" + fontStyleString + "-" + fontSize);
		String fontColorString = ((JSONObject)shapeTmp.get("font")).get("color").toString();
		Color fontColor = Color.decode(fontColorString);
		
		ColorAttributes colorAttributes = new ColorAttributes(filled, stroked, filledColor, strokedColor);
		FontAttributes fontAttributes = new FontAttributes(font, fontColor);
		SText texte = new SText(loc, text);
		
		texte.addAttributes(colorAttributes);
		texte.addAttributes(fontAttributes);
		texte.addAttributes(new SelectionAttributes());
		return texte;
	}*/
}