package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.BingModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

public class SwapInstruction implements Instruction{

	private int position1;
	private int position2;
	
	private Document responseDom;
	
	public SwapInstruction(int position1, int position2) {
		this.position1 = (position1 < position2) ? position1 : position2; //smaller one
		this.position2 = (position2 > position1) ? position2 : position1; //larger one
	}

	private void genericExecute(ModifiableSearchResultServiceProvider provider) {
		responseDom = provider.getResponseDom();
		
		System.out.println("swapping " + position1 + ", " + position2);
		if (position1 == position2) return;
		try {
			Element result1 = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath() + "/li[" + position1 + "]");
			Element result2 = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath() + "/li[" + position2 + "]");
			Element parent = result1.getParentElement();
			
			result1.detach();
			result2.detach();
			parent.addContent(position1, result2);
			parent.addContent(position2, result1);
		} catch (Exception e) {
			//TODO: logger
			//logger.error("Cannot swap results", e);
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
