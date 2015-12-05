package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.WorkflowReader;

public class SimpleAction extends ConcreteActionReader {

	public SimpleAction(Element action, WorkflowReader workflow) {
		super(action, workflow);
		
		Element simpleAction = (Element) action.getElementsByTagName("simple_action").item(0);
	}

}
