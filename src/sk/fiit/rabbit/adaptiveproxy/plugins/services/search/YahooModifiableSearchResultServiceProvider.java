package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomSenderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.DeleteInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.Instruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.MoveInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.PutInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.SwapInstruction;

public class YahooModifiableSearchResultServiceProvider implements
		ModifiableSearchResultServiceProvider {
	
	private static final String resultsParentElementPath = "";
	private static final Logger logger = Logger.getLogger(YahooModifiableSearchResultServiceProvider.class);
	
	private Document responseDom;
	private ArrayList<Instruction> instructions;
	
	public YahooModifiableSearchResultServiceProvider(Document responseDom) {
		this.responseDom = responseDom;
		this.instructions = new ArrayList<Instruction>(10);
	}

	@Override
	public void putResult(SearchResultObject result) {
		instructions.add(new PutInstruction(result));
	}

	@Override
	public void moveResult(int position, int newPosition) {
		instructions.add(new MoveInstruction(position, newPosition));
	}

	@Override
	public void swapResults(int position1, int position2) {
		instructions.add(new SwapInstruction(position1, position2));
	}

	@Override
	public void deleteResult(int position) {
		instructions.add(new DeleteInstruction(position));
	}

	@Override
	public SearchResultObject[] getSearchedData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getResultsParentElementPath() {
		return YahooModifiableSearchResultServiceProvider.resultsParentElementPath;
	}

	@Override
	public Document getResponseDom() {
		return responseDom;
	}

	@Override
	public String getServiceIdentification() {
		return this.getClass().getName();
	}

	@Override
	public ModifiableSearchResultService getService() {
		return this;
	}

	@Override
	public boolean initChangedModel() {
		return true;
	}
	
	@Override
	public void doChanges(ModifiableHttpResponse response) {
		for (Instruction instruction : instructions){
			instruction.execute(this);
		}
		response.getServicesHandle().getService(HtmlDomSenderService.class).setHTMLDom(responseDom);
	}

}
