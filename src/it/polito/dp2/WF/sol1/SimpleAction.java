package it.polito.dp2.WF.sol1;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.util.WFAttributes;
import it.polito.dp2.WF.util.WFElements;

/**
 * This is a concrete implementation of abstract class AbstractActionReader (that implements the interface ActionReader).<BR>
 * Another implementation of that abstract class is {@link it.polito.dp2.WF.sol1.ProcessAction}<BR><BR>
 * If you want more detail about the interface look to<BR>
 * {@link it.polito.dp2.WF.sol1.AbstractActionReader}<BR>
 * {@link it.polito.dp2.WF.ActionReader}
 * 
 * @author Luca
 */
public class SimpleAction extends AbstractActionReader implements SimpleActionReader {

	private Set<ActionReader> nextActions;

	public SimpleAction(Element action, WorkflowReader workflow) {
		super(action, workflow);
		
		nextActions = new HashSet<ActionReader>();
//TODO:	if(action == null) return;
		
		Element simpleAction = (Element)action.getElementsByTagName(WFElements.SIMPLE_ACTION).item(0);
//		if(simpleAction == null) return;
		
		String actionStrings = simpleAction.getAttribute(WFAttributes.ACTION_SIMPLE_NEXT);
		//taking all the strings related to next actions and check if they exists
		if( !actionStrings.equals("") ) {
			//taking the list of all the actions inside the workflow 
			Element wf = (Element)action.getParentNode();
			NodeList actions = wf.getElementsByTagName(WFElements.ACTION);
			
			//dividing the string in the different ids
			@SuppressWarnings("unused")
			String[] azioni = actionStrings.split(" ");
			
			StringTokenizer st = new StringTokenizer(actionStrings, " ");
			while(st.hasMoreTokens()) {
				String act = st.nextToken();
				
				//for each action I'm looking for its data
				for(int i=0; i<actions.getLength(); i++) {
					Element azione = (Element)actions.item(i);
					if( azione.getAttribute(WFAttributes.ACTION_ID).equals(act) ) {
						String name = azione.getAttribute(WFAttributes.ACTION_NAME);
						ActionReader ar = workflow.getAction(name);
						if( ar==null ) {
							if( azione.getElementsByTagName(WFElements.PROCESS_ACTION).getLength() >= 1 )	//"process_action"
								ar = new ProcessAction(azione, workflow);
							else
								ar = new SimpleAction(azione, workflow);
							
							((ConcreteWorkflowReader)workflow).addAction(ar);
							
						}
						nextActions.add(ar);
						//when I found the match I go the next token
						break;
					}
				}//end for on Element Actions
				
			}//end while on tokens (Actions ID)
		}//end of if 
		
	}

	@Override
	public Set<ActionReader> getPossibleNextActions() {
		return this.nextActions;
	}

}
