package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowReader;

/**
 * This is a concrete implementation of abstract class {@link AbstractActionReader} (that implements the interface ActionReader).<BR>
 * Another implementation of that abstract class is {@link ProcessAction}<br>
 * This implementation is based on JAXP framework.
 * 
 * @see {@link ActionReader}, {@link AbstractActionReader}, {@link ProcessAction}
 * @author Luca
 */
public class SimpleAction extends AbstractActionReader implements SimpleActionReader {

	private HashMap<String, ActionReader> nextActions;

	public SimpleAction(Element action, WorkflowReader workflow) {
		super(action, workflow);
		
		nextActions = new HashMap<String, ActionReader>();		
	}

	@Override
	public Set<ActionReader> getPossibleNextActions() {
		return new TreeSet<ActionReader>(nextActions.values());
	}
	

	
	public void setPossibleNextActions(Set<ActionReader> actions) {
		for(ActionReader a : actions) {
			nextActions.put(a.getName(), a);
		}
	}

	public void addPossibleNextAction(ActionReader ar) {
		nextActions.put(ar.getName(), ar);
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("NextActions: ");
		for(ActionReader ar : nextActions.values())
			buf.append(ar.getName());
		
		return super.toString()+"\n\t\t"+buf.toString();
	}

}
