package it.polito.dp2.WF.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WFUtil {

	/**
	 * This method test if the DOM {@link org.w3c.dom.Element} has insed it an Element beloging to {@link it.polito.dp2.WF.sol1.ProcessAction}.
	 * @param action - The Element that you want to check.
	 * @return <code>true</code> if the element has a ProcessAction inside it, <code>false</code> otherwise.
	 */
	public static boolean isProcessAction(Element action) {
		NodeList list = action.getElementsByTagName(WFElements.PROCESS_ACTION);
		
		if( list.getLength() >= 1 )
			return true;
		else
			return false;
	}
	
	/**
	 * This method take an {@link org.w3c.dom.Element} that have to represent an action that contains a simple_action node.
	 * The method extracts from that element a String with all the next possible actions that could be executed after the execution of this action.
	 * @param action - The Element that references a particular action.
	 * @return A String containg all the possible next actions. It returns <code>null</code> if it is not able to extract the data.
	 */
	public static String takeNextActions(Element action) {
		// gestisco le Action a cui può puntare
		NodeList list = action.getElementsByTagName(WFElements.SIMPLE_ACTION);
		if(list== null)
			return null;
		if(list.item(0).getNodeType() != Node.ELEMENT_NODE)
			return null;
		
		Element simpleAction = (Element)list.item(0);
		if(simpleAction == null)
			return null;
		//taking all the strings related to next actions and check if they exists
		return simpleAction.getAttribute(WFAttributes.ACTION_SIMPLE_NEXT);
	}
}
