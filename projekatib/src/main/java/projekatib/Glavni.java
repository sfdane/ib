package projekatib;



import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
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
	
	  
	  public void pokretanje() {
		  
		  UcitavanjeDokumenta();
		  
	  }
	  
	  
	  
	
	  
	  private Document UcitavanjeDokumenta() {

		  
		  //C:\Users\nofutur\Desktop\Nova fascikla (9)\\slika.jpg
		  while(true) {
			  try {
		  				Scanner myObj = new Scanner(System.in);
					    String path;
					    
					   
					   System.out.println("Unesi putanju++");
					    path = myObj.nextLine();   
					  
					    
					    
					    File file =new File(path);
						
						if(file.exists()){
							
							double bytes = file.length();
							System.out.println("name : " + file.getName());
							System.out.println("bytes : " + bytes);
							return null;
							
						}else{
							
						}
			  }
			  catch (NoSuchElementException e) {
				  System.out.println("File does not exists!");
			}
		  }
			
		
		  

	  }
		
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	public static void main(String[] args) {
		Glavni a= new Glavni();
		a.pokretanje();
		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(Glavni.class, args);
		
		
	}
}



