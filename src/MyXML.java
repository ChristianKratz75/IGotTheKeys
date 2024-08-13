//package IGotTheKeys;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyXML {
	public ArrayList<Class_Verzeichnisse> Vereine = new ArrayList<>();
	public ArrayList<Class_Verzeichnisse> Verzeichnisse = new ArrayList<>();
	public ArrayList<Class_Verzeichnisse> Programme = new ArrayList<>();
	
	public MyXML(String xmlfile) {
		try {
	         File inputFile = new File(xmlfile);
	         
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         
	         System.out.println("Root element :" + doc.getDocumentElement().getNodeName()); //DEBUG
	         
	         NodeList nList = doc.getElementsByTagName("Verein");
	         
	         System.out.println("----------------------------");//DEBUG

	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            System.out.println("\nCurrent Element :" + nNode.getNodeName());//DEBUG

	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               System.out.println("Verein: "
	                  + eElement.getAttribute("Name") + " Pfad: " + eElement.getAttribute("Pfad"));
	               Class_Verzeichnisse Verein = new Class_Verzeichnisse(eElement.getAttribute("Name"), 
													            		   eElement.getAttribute("Pfad"), 
													            		   eElement.getAttribute("Datenbank"),
													            		   eElement.getAttribute("Pfad_Backup")
	            		   );
	               Vereine.add(Verein);
	            }
	         }
	         
	         System.out.println("----------------------------");//DEBUG
	         nList = doc.getElementsByTagName("Verzeichnis");
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            System.out.println("\nCurrent Element :" + nNode.getNodeName());//DEBUG

	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               System.out.println("Verzeichnis: "
	                  + eElement.getAttribute("Name") + " Pfad: " + eElement.getAttribute("Pfad"));
	               Class_Verzeichnisse Verzeichnis = new Class_Verzeichnisse(eElement.getAttribute("Name"), 
	            		   														eElement.getAttribute("Pfad"));
	               Verzeichnisse.add(Verzeichnis);
	            }
	         }
	         
	         System.out.println("----------------------------");//DEBUG
	         nList = doc.getElementsByTagName("Program");
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            System.out.println("\nCurrent Element :" + nNode.getNodeName());//DEBUG

	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               System.out.println("Verzeichnis: "
	                  + eElement.getAttribute("Name") + " Pfad: " + eElement.getAttribute("Command"));
	               Class_Verzeichnisse Verzeichnis = new Class_Verzeichnisse(eElement.getAttribute("Name"), 
	            		   eElement.getAttribute("Command"),
	            		   eElement.getAttribute("Parameter"));
	               Programme.add(Verzeichnis);
	            }
	         }
	         System.out.println("----------------------------");//DEBUG
	         
		} catch (Exception e) {
	         e.printStackTrace();
	      }
	}
}
