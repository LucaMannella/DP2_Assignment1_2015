package test;

import java.io.*;
import javax.xml.parsers.*; 
import org.xml.sax.*;

/**
 * DomParseV.java
 * Validating parser with error diagnostics and warnings
 */
public class DomParseV {
    
	public static void main(String argv[])
    {
        if (argv.length != 1) {
          System.err.println("Usage: java DomParseV filename");
          System.exit(1);
        }

        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

        factory.setValidating(true);
        //--- Remove comment to deal with namespaces ---//
        // factory.setNamespaceAware(true);

        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
           builder.setErrorHandler(new DomParseVHandler());
           builder.parse( new File(argv[0]) );
           System.out.println("Parsing successful!");

        } catch (SAXParseException spe) {
            // Parsing error (file is not well formed or not valid)
            System.out.println("** Fatal parsing error"
               + ", file " + spe.getSystemId()
               + ", line " + spe.getLineNumber());
            System.out.println("   " + spe.getMessage() );
            
            // Print debug info 
            // Exception  x = spe;
            // if (spe.getException() != null)
            //     x = spe.getException(); // exception in user code
            // x.printStackTrace();
            System.exit(1);
            
        } catch (SAXException sxe) {
        	// Fatal error generated during parsing
        	System.out.println("Fatal error encountered during parsing.");

        	// Print debug info 
        	// Exception  x = sxe;
        	// if (sxe.getException() != null)
        	//     x = sxe.getException(); // exception in user code
        	// x.printStackTrace();
        	System.exit(1);

        } catch (ParserConfigurationException pce) {
        	// Error in parser configuration
        	System.out.println("Parser configuration error. Unable to proceed.");
            
        	// Print debug info
        	// pce.printStackTrace();
        	System.exit(1);

        } catch (IOException ioe) {
        	// Read error on file 
        	System.out.println("Fatal error: Unable to read file.");
        	
        	// Print debug info
        	// ioe.printStackTrace();
        	System.exit(1);
        }
    } // end of main
	
}

class DomParseVHandler extends org.xml.sax.helpers.DefaultHandler {
	
  // Validation errors are treated as fatal
  public void error (SAXParseException e) throws SAXParseException
  {
    throw e;
  }

  // Warnings are displayed (without terminating)
  public void warning (SAXParseException e) throws SAXParseException
  {
    System.out.println("** Warning"
            + ", file " + e.getSystemId()
            + ", line " + e.getLineNumber());
    System.out.println("   " + e.getMessage() );
  }
  
}
