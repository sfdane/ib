package projekatib;



import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import controller.FileUploadController;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"demo","controller"})

public class Glavni {
	 
	static {
	  	//staticka inicijalizacija
	      Security.addProvider(new BouncyCastleProvider());
	      org.apache.xml.security.Init.init();
	
  }


	public static String[] EXTENSIONS = new String[]{
	        "gif", "png", "bmp", "jpg", "jpeg" // and other formats you need
	};
	
	
	
	
	public static FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
	
	
    public static void zipFiles(List<String> files){
        
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream("./data/photos.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath:files){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                System.out.println("zipovan fail: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("gotovo");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception ex){
                 
            }
        }
    }
	
	
	
	  

	public static void main(String[] args) {

		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(Glavni.class, args);
		
		List<String> filesForZip = new ArrayList<>();

    	System.out.println("Unesite putanju: ");

    	Scanner scanner = new Scanner(System.in);
    	String putanja = scanner.nextLine();
    	scanner.close();
    	File dir = new File(putanja);
	
	
	

        if (dir.isDirectory()) { 
        	
        	try {
        		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    			Document doc = dBuilder.newDocument();
    			
    			
    			
    		
    			Element rootElement = doc.createElement("photos");
    			doc.appendChild(rootElement);
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
            	
            	
                BufferedImage img = null;

                try {
                	
                	
                	filesForZip.add(f.getAbsolutePath());
                    img = ImageIO.read(f);
                 



                    
                    Element photo = doc.createElement("photo");
                    Attr attrWidth = doc.createAttribute("width");
                    attrWidth.setValue(String.valueOf(img.getWidth()));
                    photo.setAttributeNode(attrWidth);
                    
                    Attr attrHeight = doc.createAttribute("height");
                    attrHeight.setValue(String.valueOf(img.getHeight()));
                    photo.setAttributeNode(attrHeight);
                    
                    Attr attrSize = doc.createAttribute("size");
                    attrSize.setValue(String.valueOf(f.length()));
                    photo.setAttributeNode(attrSize);
                    
                    Attr attrHashcode = doc.createAttribute("hashcode");
                    attrHashcode.setValue(String.valueOf(f.hashCode()));
                    photo.setAttributeNode(attrHashcode);
                    
                    photo.appendChild(doc.createTextNode(f.getName()));
                    
                    rootElement.appendChild(photo);
                    
                   
                } catch (final IOException e) {

                    e.printStackTrace();

                }
            }
            
            
            
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File xmlFile = new File("./data/photos.xml");

            
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);            
            
            filesForZip.add(xmlFile.getAbsolutePath());
          
            
            zipFiles(filesForZip);

        	} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
        } else {
        	System.out.println("Nije folder");
        }
    }
	
	
	
	


}


