package it.polito.dp2.WF.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class DomUtil {

	/**  
	 * This method create a DOM Document object.
	 * @param validating - True if you want that the document is validated otherwise false.
	 * @return The DOM Document
	 * @throws ParserConfigurationException 
	 */
	public static Document createDOMDocument(boolean validating) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}
	
}
