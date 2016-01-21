package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.DomUtil;
import it.polito.dp2.WF.sol1.util.WFAttributes;
import it.polito.dp2.WF.sol1.util.WFElements;

/**
 * This is a concrete implementation of the interface {@link WorkflowMonitor} based on the JAXP framework.
 * 
 * @author Luca
 */
public class ConcreteWorkflowMonitor implements it.polito.dp2.WF.WorkflowMonitor {

	private HashMap<String, ProcessReader> processes;
	private HashMap<String, WorkflowReader> workflows;
	private HashMap<String, Actor> actors;

	public ConcreteWorkflowMonitor() throws SAXParseException { /* --- default constructor --- */
		String inputFileName = System.getProperty("it.polito.dp2.WF.sol1.WFInfo.file");
		
		Document doc = DomUtil.parseDomDocument(inputFileName, true);		
		if(doc == null) {
			System.err.println("The document is null, something wrong happens!");
			System.exit(12);
		}
		
		NodeList list = doc.getElementsByTagName(WFElements.WORKFLOW_MANAGER);
		if( (list.item(0) == null) || (list.item(0).getNodeType() != Node.ELEMENT_NODE) ) {
			System.err.println("Something wrong in the root element!");
			System.exit(13);
		}
		
		Element root = (Element) list.item(0);
		setParameter(root);
	}
	
	//if I valid the document before creating the element I can assume some stuff
	public ConcreteWorkflowMonitor(Set<ProcessReader> processes, Set<WorkflowReader> workflows, Set<Actor> actors)
																	throws IllegalArgumentException {
		if( (processes == null) || (processes.size() == 0) )
    		throw new IllegalArgumentException("Wrong parameter, \"processes\" was null or empty!");
		
		if( (workflows == null) || (workflows.size() == 0) )
			throw new IllegalArgumentException("Wrong parameter, \"workflows\" was null or empty!");
		
		this.processes = new HashMap<String, ProcessReader>();
		int code = 1;	//TODO: maybe could be something wrong on this use of code...
		for(ProcessReader proc : processes) {
			this.processes.put("p"+code, proc);
			code++;
		}
		this.workflows = new HashMap<String, WorkflowReader>();
		for(WorkflowReader wfr : workflows) {
			this.workflows.put(wfr.getName(), wfr);
		}
		this.actors = new HashMap<String, Actor>();
		for(Actor a : actors) {
			this.actors.put(a.getName(), a);
		}
	}
	
	public ConcreteWorkflowMonitor(Element element) throws SAXParseException {
		setParameter(element);
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return new TreeSet<ProcessReader>(processes.values());
	}

	@Override
	public WorkflowReader getWorkflow(String name) {
		return workflows.get(name);
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {
		return new TreeSet<WorkflowReader>(workflows.values());
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Inside this WorkflowMonitor there are:\n");
		
		buf.append("--- Workflows ---\n");
		if((workflows==null) || (workflows.isEmpty()))
			buf.append("\tNo Workflows\n");
		else {
			for(WorkflowReader wfr : workflows.values())
				buf.append(wfr.toString()+"\n");
		}
		buf.append("\n");
		
		buf.append("--- Processes ---\n");
		if((processes==null) || (processes.isEmpty()))
			buf.append("\tNo Processes\n");
		else {
			for(ProcessReader pr : processes.values())
				buf.append(pr.toString()+"\n");
		}
		buf.append("\n");
		
		buf.append("--- Actors ---\n");
		if((actors==null) || (actors.isEmpty()))
			buf.append("\tNo Actors\n");
		else {
			for(Actor a : actors.values())
				buf.append("\t"+a.toString()+"\n");
		}
		buf.append("\n\n");
		
		return buf.toString();
	}

	/**
	 * This method is a shorter version of the toString method.
	 * @return A string that represent the object.
	 */
	public String toShortString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		
		StringBuffer buf = new StringBuffer("Inside this WorkflowMonitor there are:\n");
		
		buf.append("--- Workflows ---\n");
		if((workflows==null) || (workflows.isEmpty()))
			buf.append("\tNo Workflows\n");
		else {
			for(WorkflowReader wfr : workflows.values()) {
				buf.append("\t"+wfr.getName()
						+" has "+wfr.getActions().size()+" actions and "
						+wfr.getProcesses().size()+" processes \n");
			}
		}
		buf.append("\n");
		
		buf.append("--- Processes ---\n");
		if((processes==null) || (processes.isEmpty()))
			buf.append("\tNo Processes\n");
		else {
			for(ProcessReader pr : processes.values()) {
				buf.append("\t prosses belonging to <"+pr.getWorkflow().getName()
					+"> started at <"+dateFormat.format(pr.getStartTime().getTime())
					+"> has "+pr.getStatus().size()+" action status\n");
			}
		}
		buf.append("\n\n");
		
		buf.append("--- Actors ---\n");
		if((actors==null) || (actors.isEmpty()))
			buf.append("\tNo Actors\n");
		else {
			for(Actor a : actors.values())
				buf.append("\t"+a.toString()+"\n");
		}
		buf.append("\n\n");
		
		return buf.toString();
	}
	
	public Actor getActor(String name) {
		return actors.get(name);
	}
	
	public Set<Actor> getActors() {
		return new TreeSet<Actor>(actors.values());
	}
	
	public void setParameter(Element root) throws SAXParseException {
		if (root == null)
    		throw new IllegalArgumentException("Wrong parameter, element was null!");
    	
		workflows = new HashMap<String, WorkflowReader>();
		processes = new HashMap<String, ProcessReader>();
		actors = new HashMap<String, Actor>();
		int i=0;
		NodeList wfNodes = root.getElementsByTagName(WFElements.WORKFLOW);		//"workflow"
		NodeList procNodes = root.getElementsByTagName(WFElements.PROCESS);		//"process"
		
		/* workflows */
		System.out.println("DEBUG - In the document there are "+wfNodes.getLength()+" workflows");
	    for (i=0; i<wfNodes.getLength(); i++) {
	    	if(wfNodes.item(i) instanceof Element) {	//if I don't take an element I ignore it
	    		WorkflowReader wf = new ConcreteWorkflowReader((Element)wfNodes.item(i), procNodes);
	    		workflows.put(wf.getName(), wf);
	    	}
	    }
		System.out.println("DEBUG - Workflows created");
		
		/* processes */
		System.out.println("DEBUG - In the document there are "+procNodes.getLength()+" processes");
		int code = 1;
		for (i=0; i<procNodes.getLength(); i++) {
			if(procNodes.item(i) instanceof Element) {	//if I don't take an element I ignore it
				Element e = (Element) procNodes.item(i);
				//I should have already the workflow inside the hashmap (document should be valid)
				WorkflowReader myWF = workflows.get(e.getAttribute(WFElements.WORKFLOW));
				
		    	ProcessReader proc = new ConcreteProcessReader(e, myWF);		    	
		    	processes.put("p"+code, proc);
		    	System.out.println("DEBUG - I'm the process p"+code+" - My workflow is: "+myWF.getName());
		    	code++;
			}
	    }
		System.out.println("DEBUG - Processes created");
		
		/* actors */	//TODO: update this part if you want to manage more departments		
		NodeList actorsNodes = root.getElementsByTagName( WFElements.ACTORS );		//"actors"
		System.out.println("DEBUG - Number of tag actors: "+actorsNodes.getLength());
		// this loop is executed just one time in this particular application
		for(i=0; i<actorsNodes.getLength(); i++) {
			if(actorsNodes.item(i) instanceof Element) {	//if I don't take an element I ignore it
				Element e = (Element) actorsNodes.item(i);
				NodeList acts = e.getElementsByTagName( WFElements.ACTOR );			//"actor"
				System.out.println("DEBUG - Number of actor: "+acts.getLength());
				for(int j=0; j<acts.getLength(); j++) {
					if(acts.item(i) instanceof Element) {	//if I don't take an element I ignore it
						e = (Element) acts.item(j);
						String name = e.getAttribute( WFAttributes.ACTOR_NAME );
						String role = e.getAttribute( WFAttributes.ACTOR_ROLE );
						
						name = name.replaceAll("_", " ");
						Actor a = new Actor(name, role);						
						actors.put(a.getName(), a);
					}
				}
			}
		}
		System.out.println("DEBUG - Actors created");
		
		return;
	}

}