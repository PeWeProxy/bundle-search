package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultObject;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.BingModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

public class PutInstruction implements Instruction {
	private static final Logger logger = Logger.getLogger(PutInstruction.class);
	
	private SearchResultObject result;
	private int position;
	private Document responseDom;
	
	public PutInstruction(SearchResultObject result, int position) {
		this.result = result;
		this.position = position;
	}

	public void genericExecute(ModifiableSearchResultServiceProvider provider){
		responseDom = provider.getResponseDom();
		logger.info("Putting result to position " + position);
	}
	
	public void execute(GoogleModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);
		try {
			Element ol = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ol.addContent(position, provider.puzzleResultElement(result));
		} catch (Exception e) {
			logger.error("Cannot put result", e);
		}
	}

	@Override
	public void execute(YahooModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);
		try {
			Element ol = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ol.addContent(position - 1, provider.puzzleResultElement(result)); //works better for Yahoo! with minus one 
		} catch (Exception e) {
			logger.error("Cannot put result", e);
		}
		
	}

	@Override
	public void execute(BingModifiableSearchResultServiceProvider provider) {
		genericExecute(provider);
		try {
			Element ul = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ul.addContent(position - 1, provider.puzzleResultElement(result)); //works better for Bing with minus one
		} catch (Exception e) {
			logger.error("Cannot put result", e);
		}
	}

}
