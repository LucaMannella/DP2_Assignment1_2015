package it.polito.dp2.WF.sol1.util;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DomParseErrorHandler extends DefaultHandler {

	@Override	// Warnings are displayed (without terminating)
	public void warning(SAXParseException exception) throws SAXException {
		System.out.println("** Warning"
	            + ", file " + exception.getSystemId()
	            + ", line " + exception.getLineNumber());
		System.out.println("   " + exception.getMessage() );
	}

	@Override	// Validation errors are treated as fatal
	public void error(SAXParseException exception) throws SAXException {
		throw exception;
	}	 

}
