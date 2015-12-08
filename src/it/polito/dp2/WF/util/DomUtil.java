package it.polito.dp2.WF.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DomUtil {

	/**  
	 * This method create a DOM Document object.
	 * @param validating - True if you want that the document is validated otherwise false.
	 * @return The DOM Document
	 * @throws ParserConfigurationException 
	 */
	public static Document createEmptyDOMDocument(boolean validating) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}
	
	/**
	 * This method parse the specified XML document an it returns a DOM instance.
	 * 
	 * @param inputFileName The name of the file to parse
	 * @param validating Specify true if you want that the document was validated
	 * 
	 * @return A DOM object representing the XML file
	 */
	public static Document parseDomDocument(String inputFileName, boolean validating){
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setValidating(validating);
			
		Document document = null;
		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			document = builder.parse(inputFileName);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
	}
	
}
