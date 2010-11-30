package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.BingModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

public class MoveInstruction implements Instruction {
	private static final Logger logger = Logger.getLogger(MoveInstruction.class);

	private int position;
	private int newPosition;
	
	private Document responseDom;
	
	public MoveInstruction(int position, int newPosition) {
		this.position = position;
		if(newPosition > position)
			this.newPosition = newPosition - 1; //because the result will be detached from original position
		else
			this.newPosition = newPosition;
	}

	private void genericExecute(ModifiableSearchResultServiceProvider provider) {
		responseDom = provider.getResponseDom();
		
		System.out.println("moving from " + position + " to " + newPosition);
		if (position == newPosition) return;
		try {
			Element result = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath() + "/li[" + position + "]");
			Element parent = result.getParentElement();
			
			result.detach();
			parent.addContent(newPosition, result);
		} catch (Exception e) {
			logger.error("Cannot move result", e);
		}
	}

	@Override
	public void execute(GoogleModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);		
	}

	@Override
	public void execute(YahooModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);
	}

	@Override
	public void execute(BingModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);
	}
}
