package it.polito.dp2.WF.sol1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
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

	public ConcreteActionStatusReader(Element action, String wfName) {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS z");
		//dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
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
		else {
			Element root = (Element) action.getParentNode().getParentNode();
			NodeList actorsNodes = root.getElementsByTagName( WFElements.ACTORS );
			
			// this loop is executed just one time in this particular application
			actor = null;
			for(int i=0; i<actorsNodes.getLength(); i++) {
				Element e = (Element) actorsNodes.item(i);
				NodeList acts = e.getElementsByTagName( WFElements.ACTOR );
				
				for(int j=0; j<acts.getLength(); j++) {
					Element a = (Element) acts.item(j);
					//confronting the string taken from the element <actor name="name_surname"> with the one taken from <action_status> 
					if(a.getAttribute( WFAttributes.ACTOR_NAME ).equals(actorName)) {
						actorName = actorName.replaceAll("_", " ");
						this.actor = new Actor(actorName, a.getAttribute( WFAttributes.ACTOR_ROLE ));
						break;
					}
				}
			}
			if(actor == null)
				System.err.println("The actor is still null... Something wrong in the document!");
			
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
