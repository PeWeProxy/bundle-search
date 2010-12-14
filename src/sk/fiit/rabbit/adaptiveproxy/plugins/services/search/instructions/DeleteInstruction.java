package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.BingModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

public class DeleteInstruction implements Instruction {
	private static final Logger logger = Logger.getLogger(DeleteInstruction.class);

	private int position;
	private Document responseDom;
	
	public DeleteInstruction(int position){
		this.position = position;
	}
	

	private void genericExecute(ModifiableSearchResultServiceProvider provider){
		responseDom = provider.getResponseDom();
		
		System.out.println("deleting " + position);
		try {
			Element result = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath() + "/li[" + position + "]");
			if (result != null){
				result.detach();
			}
		} catch (Exception e) {
			logger.error("Cannot delete result", e);
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
