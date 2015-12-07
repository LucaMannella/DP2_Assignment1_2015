package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.WorkflowReader;

/**
 * This is a concrete implementation of abstract class AbstractActionReader (that implements the interface ActionReader).<BR>
 * Another implementation of that abstract class is {@link it.polito.dp2.WF.sol1.ProcessAction}<BR><BR>
 * If you want more detail about the interface look to<BR>
 * {@link it.polito.dp2.WF.sol1.AbstractActionReader}<BR>
 * {@link it.polito.dp2.WF.ActionReader}
 * 
 * @author Luca
 */
public class SimpleAction extends AbstractActionReader {

	public SimpleAction(Element action, WorkflowReader workflow) {
		super(action, workflow);
		
		Element simpleAction = (Element) action.getElementsByTagName("simple_action").item(0);
	}

}
