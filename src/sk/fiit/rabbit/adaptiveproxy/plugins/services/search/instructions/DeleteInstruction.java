package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;

public class DeleteInstruction implements Instruction {

	private int position;
	private Document responseDom;
	
	public DeleteInstruction(int position){
		this.position = position;
	}
	
	@Override
	public void execute(GoogleModifiableSearchResultServiceProvider provider){
		responseDom = provider.getResponseDom();
		
		System.out.println("deleting " + position);
		try {
			Element result = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath() + "/li[" + position + "]");
			if (result != null){
				result.detach();
			}
		} catch (Exception e) {
			//TODO: logger?
			//logger.error("Cannot delete result", e);
		}
	}

}
