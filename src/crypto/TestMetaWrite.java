package crypto;

import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.imageio.plugins.common.ImageUtil;

public class TestMetaWrite
{

	
	private static Node treeRoot;
	private IIOMetadata metadata;
	public TestMetaWrite(){
		
	}
	
	/// READ !!
	private void readAndDisplayMetadata( String fileName ) {
		try {

            File file = new File( fileName );
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext()) {

                // pick the first available ImageReader
                ImageReader reader = readers.next();

                // attach source to the reader
                reader.setInput(iis, true);

                // read metadata of first image
                //IIOMetadata metadata = reader.getImageMetadata(0);
                metadata = reader.getImageMetadata(0);

                String[] names = metadata.getMetadataFormatNames();
                int length = names.length;
                for (int i = 0; i < length; i++) {
                    System.out.println( "Format name: " + names[ i ] );
                    treeRoot = metadata.getAsTree(names[i]);
                    displayMetadata(treeRoot);//metadata.getAsTree(names[i]));
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    void indent(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("    ");
    }

    void displayMetadata(Node node, int level) {
        // print open tag of element
        indent(level);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) {

            // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                //
                if(node.getNodeName()=="CompressionTypeName"){
                	attr.setNodeValue("1 1 1 ");
                }
                // 
                System.out.print(" " + attr.getNodeName() +
                                 "=\"" + attr.getNodeValue() + "\"");
            }
        }

        Node child = node.getFirstChild();
        if (child == null) {
            // no children, so close element and return
            System.out.println("/>");
            return;
        }

        // children, so close current tag
        System.out.println(">");
        while (child != null) {
            // print children recursively
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }
        
        // print close tag of element
        indent(level);
        System.out.println("</" + node.getNodeName() + ">");
    }
    
    public void writeMetadata() throws IOException{
    	ImageWriter writer = ImageIO.getImageWritersByFormatName("BMP").next(); 
    	
    	File f =  new File("C:/PERSO/hist.bmp");
        ImageOutputStream ios = ImageIO.createImageOutputStream(f); 
        writer.setOutput(ios); 
 

    	BufferedImage bi = ImageIO.read(new File("C:/PERSO/test.bmp"));
        /*
        ImageWriteParam param = writer.getDefaultWriteParam(); 
        ImageTypeSpecifier type = new ImageTypeSpecifier(bi); 
 
        IIOMetadata imgMetadata = writer.getDefaultImageMetadata(type, param); 
         */
    	String format = metadata.getNativeMetadataFormatName();
        
    	metadata.mergeTree(format, treeRoot);
    	
        IIOImage iio_img = new IIOImage(bi, null, metadata); 
 
        writer.write(iio_img);
        ios.flush(); 
        ios.close(); 
    }
    
    
    
    /// WRITE !!
    
    private IIOMetadataNode addChildNode(IIOMetadataNode root, String name, String value) throws IOException {
    	IIOMetadataNode child = new IIOMetadataNode(name);
    	if (value != null) {
   			child.setUserObject((Object)value);
   			child.setNodeValue(value);
    	}
    	root.appendChild(child);
    	child.setNodeValue(value);

		System.out.println("************************************");
    	this.displayMetadata(root,0);
    	this.writeMetadata();
    	return child;
    }

    
    
    ///MAIN !!
    public static void main(String[] args) throws IOException {
		TestMetaWrite test = new TestMetaWrite();
		test.readAndDisplayMetadata("C:/PERSO/test.bmp");
		test.addChildNode((IIOMetadataNode)treeRoot,"TEST","0000000");
		System.out.println("************************************");

		test.readAndDisplayMetadata("C:/PERSO/hist.bmp");
		//test.readAndDisplayMetadata("C:/PERSO/hist.bmp");
	}
	
}
