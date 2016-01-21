package it.polito.dp2.WF.sol1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFAttributes;
import it.polito.dp2.WF.sol1.util.WFElements;

/**
 * This is a concrete implementation of the interface {@link ActionStatusReader} based on the JAXP framework.
 * 
 * @author Luca
 */
public class ConcreteActionStatusReader implements ActionStatusReader {
	
	private SimpleDateFormat dateFormat;
	
	private String name;
	private Actor actor;
	private Calendar endTime;
	private boolean takenInCharge;
	private boolean terminated;

	/**
	 * This method create an implementation of a {@link ActionStatusReader} interface. 
	 * 
	 * @param action - An {@link Element} that refer an action.
	 * @param wfName - The name of the {@link WorkflowReader}
	 * @param act - The {@link Actor} that should perform the action
	 * @throws SAXException If the selected actor is not able to perform the action
	 */
	public ConcreteActionStatusReader(Element action, String wfName) throws SAXException  {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS z");
		endTime = Calendar.getInstance();

//TODO:	if((action == null) || (wfName == null)) return;	//safety lock
		String name = action.getAttribute( WFAttributes.ACTION_STATUS_NAME );		//"action"
		this.name = name.replace(wfName+"_", "");
		
		String timestamp = action.getAttribute( WFAttributes.ACTION_STATUS_TIME );	//"timestamp"
		String actorName = action.getAttribute( WFAttributes.ACTION_STATUS_ACTOR );	//"actor"
		
		if(actorName.equals("")) {
			actor = null;
			terminated = false;
			takenInCharge = false;
			endTime.setTimeInMillis(0);
		}
		else {	//if we are here the action is takenInCharge or terminated
			Element root = (Element) action.getParentNode().getParentNode();
			
			NodeList actorsNodes = root.getElementsByTagName( WFElements.ACTORS );
			
			// this loop is executed just one time in this particular application
			actor = null;	//taking the actor from the document
			for(int i=0; i<actorsNodes.getLength(); i++) {
				Element e = (Element) actorsNodes.item(i);
				NodeList acts = e.getElementsByTagName( WFElements.ACTOR );
				
				for(int j=0; j<acts.getLength(); j++) {
					Element a = (Element) acts.item(j);
					//confronting the string taken from the element <actor name="name_surname"> with the one taken from <action_status> 
					if(a.getAttribute( WFAttributes.ACTOR_NAME ).equals(actorName)) {
						actorName = actorName.replaceAll("_", " ");
						this.actor = new Actor(actorName, a.getAttribute( WFAttributes.ACTOR_ROLE ));
						System.out.println("Actor name: "+actor.getName()+" actor role: "+actor.getRole());
						break;	
						//TODO: modify if you want to manage more departments
					}
				}
			}
			if(actor == null)
				System.err.println("The actor is still null... Something wrong in the document!");
	//here		
			// retrieving the expected role for the ActionStatus
			NodeList workflows = root.getElementsByTagName(WFElements.WORKFLOW);
			for(int i=0; i<workflows.getLength(); i++) {
				if(workflows.item(i) instanceof Element == false)
					continue;
				
				Element wf = (Element) workflows.item(i);
				String anotherWFName = wf.getAttribute( WFAttributes.WORKFLOW_NAME );
				
				if(wfName.equals(anotherWFName)) {
					NodeList actions = wf.getElementsByTagName(WFElements.ACTION);
					for(int j=0; j<actions.getLength(); j++) {
						if(actions.item(j) instanceof Element == false)
							continue;
						
						Element act = (Element) actions.item(j);
						String actionName = act.getAttribute(WFAttributes.ACTION_NAME).replace(wfName+"_", "");
						String expectedRole = act.getAttribute(WFAttributes.ACTION_ROLE);
						
						if(this.name.equals(actionName)) {
							if( actor.getRole().equals(expectedRole) == false) {
								String errorMessage = "The actor "+actor.getName()+" is not a "+expectedRole+" and he can not execute the action "+this.name+"!"; 
								System.out.println(errorMessage);
								
								throw new SAXException(errorMessage);
							}
						}
					}
				}
			}	//if the actor can't manage this action a SAXException is thrown
		
//here			
			takenInCharge = true;
			
			if(timestamp.equals( WFAttributes.STATUS_NOT_FINISHED )) {	//if (timestamp=="Not Finished")
				terminated = false;
				endTime.setTimeInMillis(0);
			}
			else {
				try {	//everything all right
					endTime.setTime(dateFormat.parse(timestamp));
					terminated = true;
				}
				catch (ParseException e) {	//something wrong happens
					terminated = false;
					takenInCharge = true;
					endTime.setTimeInMillis(0);
					System.err.println("Error parsing the action, this action is considered as Not Finished");
				}
			}
		}
		
	}

	@Override
	public String getActionName() {
		return this.name;
	}

	@Override
	public Actor getActor() {
		return actor;
	}

	@Override
	public Calendar getTerminationTime() {
		return endTime;
	}

	@Override
	public boolean isTakenInCharge() {
		return takenInCharge;
	}

	@Override
	public boolean isTerminated() {
		return terminated;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("Action name: "+this.name);
		if(takenInCharge) {
			buf.append(" - taken in charge by: "+actor.getName());
			if(terminated)
				buf.append(" - terminated at: "+dateFormat.format(endTime.getTimeInMillis()));
			else
				buf.append(" - not yet terminated");
		}
		else
			buf.append(" - not taken in charge by anyone");

		return buf.toString();
	}

}
