package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

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
	
	private static final String resultsParentElementPath = "/html/body/div[@id='doc']/div[@role='document']/div[@id='results']/div[@id='cols']/div[@id='left']/div[@id='main']/div[@id='web']/ol";
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
	public ArrayList<SearchResultObject> getSearchedData() {
		YahooSearchResultServiceProvider readingProvider = new YahooSearchResultServiceProvider(responseDom); 
		return readingProvider.getSearchedData();
	}

	@Override
	public String getQueryString() {
		YahooSearchResultServiceProvider readingProvider = new YahooSearchResultServiceProvider(responseDom); 
		return readingProvider.getQueryString();
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
	public Element puzzleResultElement(SearchResultObject result) {
		
		Element li = new Element("li");
		
		Element div_res = new Element("div");
		div_res.setAttribute("class", "res");
		li.addContent(div_res);
		
		Element div = new Element("div");
		div_res.addContent(div);
		
		Element h3 = new Element("h3");
		div.addContent(h3);
		
		Element a_yschttl_spt = new Element("a");
		a_yschttl_spt.setAttribute("class", "yschttl spt");
		a_yschttl_spt.setAttribute("href", result.getUrl());
		a_yschttl_spt.setText(result.getHeader());
		h3.addContent(a_yschttl_spt);
		
		Element div_abstr = new Element("div");
		div_abstr.setAttribute("class", "abstr");
		div_abstr.setText(result.getPerex());
		div_res.addContent(div_abstr);
		
		Element span_url = new Element("span");
		span_url.setAttribute("class", "url");
		span_url.setText(result.getShortUrl());
		div_res.addContent(span_url);
		
		return li;
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
