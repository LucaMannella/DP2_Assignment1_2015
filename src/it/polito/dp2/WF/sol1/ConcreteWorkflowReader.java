package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFAttributes;
import it.polito.dp2.WF.sol1.util.WFElements;
import it.polito.dp2.WF.sol1.util.WFUtil;

/**
 * This is a concrete implementation of the interface {@link WorkflowReader} based on the JAXP framework.
 * 
 * @author Luca
 */
public class ConcreteWorkflowReader implements WorkflowReader, Comparable<WorkflowReader> {

	private String name;
	private Map<String, ActionReader> actions;
	private Set<ProcessReader> processes;

	/**
	 * This method create an implementation of the {@link WorkflowReader} interface.
	 * @param workflow - The {@link Process} starting object.
	 * @param workflow - The {@link WorkflowReader} whom this process belongs.
	 * @throws SAXException If an actor is not able to perform an action.
	 */
	public ConcreteWorkflowReader(Element workflow, NodeList procNodes) throws SAXException {
		actions = new HashMap<String, ActionReader>();
		processes = new HashSet<ProcessReader>();
		
		if(workflow == null) return;	//safety lock
		
		this.name = workflow.getAttribute( WFAttributes.WORKFLOW_NAME );				//"name"
		
		// set the actions inside the object
		NodeList actionNodes = workflow.getElementsByTagName( WFElements.ACTION );		//"action"
		for (int i=0; i<actionNodes.getLength(); i++) {
			
			if(actionNodes.item(i) instanceof Element) {		//if it's not an element I ignore it
				Element azione = (Element) actionNodes.item(i);
				
				if(! actions.containsKey( azione.getAttribute(WFAttributes.ACTION_NAME) )) {
					//I create a new action only if they does not still exists
					if( WFUtil.isProcessAction(azione) ) {
						ActionReader ar = new ProcessAction(azione, this);
						actions.put(ar.getName(), ar);
					}
					else {
						SimpleAction sar = new SimpleAction(azione, this);
						actions.put(sar.getName(), sar);
						
						String nextActionStr = WFUtil.takeNextActions(azione);
						if((nextActionStr != null)&&( !nextActionStr.equals("") )) {
							
							//dividing the string in the different ids
							String[] nextActionStrings = nextActionStr.split(" ");
							for(String nextAct : nextActionStrings) {
								//for each action I'm looking for its data
								
								for(int j=0; j<actionNodes.getLength(); j++) {
									
									Element act = (Element)actionNodes.item(j);
									if( act.getAttribute( WFAttributes.ACTION_ID ).equals(nextAct) ) {
										String name = act.getAttribute( WFAttributes.ACTION_NAME );
										ActionReader ar = actions.get(name);
										if( ar==null ) {
											if( WFUtil.isProcessAction(act) )
												ar = new ProcessAction(act, this);
											else
												ar = new SimpleAction(act, this);
											
											actions.put(ar.getName(), ar);											
										}
										sar.addPossibleNextAction(ar);
										
										break;
									}
								}//end FOR on Element Actions
								
							}//end FOR on tokens (Actions ID)
						}//go directly here if I don't have next Actions
						
					}// end of the manage of the SimpleAction
				}
	    	}
		}
		
		if(procNodes == null) return;		//safety lock
		
		// set the processes that refer this Workflow
		for(int i=0; i<procNodes.getLength(); i++) {
			if(procNodes.item(i) instanceof Element) {
				Element e = (Element) procNodes.item(i);
				if( e.getAttribute(WFAttributes.PROCESS_WFNAME).equals(this.name) ) {
					//if(process.workflowName == workflow.name) 
					ProcessReader p = new ConcreteProcessReader(e, this);
					processes.add(p);
				}
			}
		}
		
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<ActionReader> getActions() {
		return new TreeSet<ActionReader>(actions.values());
	}

	@Override
	public ActionReader getAction(String actionName) {
		return actions.get(actionName);
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return processes;
	}

	@Override
	public int compareTo(WorkflowReader o) {
		return this.name.compareTo(o.getName());
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("Workflow: "+name+"\n");
		
		buf.append("Actions:\n");
		for(ActionReader ar : actions.values()) {
			buf.append("\t"+ar.toString()+"\n");
		}
		
		buf.append("\tProcesses:\n");
		if(processes.isEmpty()){
			buf.append("\t\t No Processes \n");
		}
		else {
			for(ProcessReader pr : processes) {
				if(pr instanceof ConcreteProcessReader) {
					ConcreteProcessReader cpr = (ConcreteProcessReader) pr;
					buf.append("\t\t"+cpr.toShortString()+"\n");
				}
				else {
					buf.append("\t\t"+pr.toString()+"\n");
				}
			}
		}
		
		return buf.toString();
	}
	
}
