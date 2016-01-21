package it.polito.dp2.WF.sol1.util;

import java.io.*;
import javax.xml.parsers.*; 
import org.xml.sax.*;

/**
 * DomParseValidator.java
 * Validating parser with error diagnostics and warnings
 */
public class DomParseValidator {
    
	public static void main(String argv[])
    {
        if (argv.length != 1) {
          System.err.println("Usage: java DomParseValidator filename");
          System.exit(1);
        }

        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

        factory.setValidating(true);

        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
           builder.setErrorHandler(new DomParseErrorHandler());
           builder.parse( new File(argv[0]) );
           System.out.println("Parsing successful!");

        } catch (SAXParseException spe) {
            // Parsing error (file is not well formed or not valid)
            System.out.println("** Fatal parsing error"
               + ", file " + spe.getSystemId()
               + ", line " + spe.getLineNumber());
            System.out.println("   " + spe.getMessage() );
            
            System.exit(1);
        }
        catch (SAXException sxe) {
        	// Fatal error generated during parsing
        	System.out.println("Fatal error encountered during parsing.");
          	System.exit(1);
        }
        catch (ParserConfigurationException pce) {
        	// Error in parser configuration
        	System.out.println("Parser configuration error. Unable to proceed.");
        	System.exit(1);
        }
        catch (IOException ioe) {
        	// Read error on file 
        	System.out.println("Fatal error: Unable to read file.");
        	System.exit(1);
        }
    }
  
}
