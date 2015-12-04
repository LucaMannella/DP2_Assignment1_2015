package it.polito.dp2.WF.sol1;

import java.util.Set;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class ConcreteWorkflowMonitor implements it.polito.dp2.WF.WorkflowMonitor {
	
	//if I valid the document before creating the element i can assume some stuff
	
	//default constructor
	public ConcreteWorkflowMonitor() {	}
	
	public ConcreteWorkflowMonitor(Element element) {
		if (element == null)
    		return; 
    	else {	//TODO: implement the constructor for the element
    	    /*ISBN = element.getAttribute("ISBN");
    	    
    	    //cerca un nodo all'interno dell'elemento con quel particolare tag name 
    	    NodeList authorNodes = element.getElementsByTagName("author");
    	    int authorNumber = authorNodes.getLength();
    	    authors = new String[authorNumber];
    	    for (int i=0; i < authorNumber; i++) {
    			authors[i] = authorNodes.item(i).getFirstChild().getNodeValue();
    	    }
    	    
			title = element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			*/
    	}
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowReader getWorkflow(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {
		// TODO Auto-generated method stub
		return null;
	}

}
