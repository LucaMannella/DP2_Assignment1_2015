package it.polito.dp2.WF.sol1.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
	public static Document parseDomDocument(String inputFileName, boolean validating) {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setValidating(validating);
			
		Document document = null;
		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			builder.setErrorHandler( new DomParseErrorHandler() );
			document = builder.parse(inputFileName);
		} 
		catch (SAXException se) {
			System.err.println("\n** Internal error.");
			System.err.println("\t"+se.getMessage());
			se.printStackTrace();
			System.exit(22);
		}
		catch (ParserConfigurationException pce) {
			System.err.println("\n** Internal error.");
			System.err.println("\t Parser with specified options can't be built: "+pce.getMessage());
			pce.printStackTrace();
			System.exit(23);
		}
		catch (IOException ioe) {
			// I/O error
			System.err.println("Unable to open/read the file");
			System.err.println("Error message: "+ioe.getMessage());
			ioe.printStackTrace();
			System.exit(11);
		}
		
		return document;
	}
	
	/**
	 * This method take all the elements form the element <code>e</code> with the related tag <code>TagName</code>.
	 * The method check also that this element have to be of type {@link org.w3c.dom.Element} otherwise it returns <code>null</code>.
	 * 
	 * @param e - The element that you want to analize
	 * @param TagName - The related tag name
	 * 
	 * @return It returns the related Element otherwise null.
	 */
	public static Element getFirstElementByTagName(Element e, String TagName) {
		NodeList list = e.getElementsByTagName(TagName);
		if(list.item(0) == null)
			return null;
		if(list.item(0).getNodeType() != Node.ELEMENT_NODE)
			return null;
					
		return (Element) list.item(0);
	}
	
}
