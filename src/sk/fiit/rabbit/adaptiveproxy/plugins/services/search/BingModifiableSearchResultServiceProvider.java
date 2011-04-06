package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomWriterService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultObject;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.DeleteInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.Instruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.MoveInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.PutInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.SwapInstruction;

public class BingModifiableSearchResultServiceProvider implements
		ModifiableSearchResultServiceProvider {
	
	private static final String resultsParentElementPath = "/html/body/div[@id='sw_page']/div[@id='sw_width']/div[@id='sw_content']/div/div[@id='sw_canvas']/div[@id='sw_main']/div[@id='content']/div[@id='results_area']/div[@id='results_container']/div[@id='results']/ul";
	private static final Logger logger = Logger.getLogger(YahooModifiableSearchResultServiceProvider.class);

	private Document responseDom;
	private ArrayList<Instruction> instructions;
	
	public BingModifiableSearchResultServiceProvider(Document responseDom) {
		this.responseDom = responseDom;
		this.instructions = new ArrayList<Instruction>(10);
	}
	
	@Override
	public void putResult(SearchResultObject result, int position) {
		instructions.add(new PutInstruction(result, position));
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
		BingSearchResultServiceProvider readingProvider = new BingSearchResultServiceProvider(responseDom); 
		return readingProvider.getSearchedData();
	}

	@Override
	public String getQueryString() {
		BingSearchResultServiceProvider readingProvider = new BingSearchResultServiceProvider(responseDom); 
		return readingProvider.getQueryString();
	}
	
	@Override
	public String getResultsParentElementPath() {
		return BingModifiableSearchResultServiceProvider.resultsParentElementPath;
	}

	@Override
	public Document getResponseDom() {
		return responseDom;
	}
	
	@Override
	public Element puzzleResultElement(SearchResultObject result) {
		
		Element li_sa_wr = new Element("li");
		li_sa_wr.setAttribute("class", "sa_wr");
		
		Element div_sa_cc = new Element("div");
		div_sa_cc.setAttribute("class", "sa_cc");
		li_sa_wr.addContent(div_sa_cc);
		
		Element div_sb_tlst = new Element("div");
		div_sb_tlst.setAttribute("class", "sb_tlst");
		div_sa_cc.addContent(div_sb_tlst);
		
		Element h3 = new Element("h3");
		div_sb_tlst.addContent(h3);
		
		Element a = new Element("a");
		a.setAttribute("href", result.getUrl());
		if(!"".equals(result.getOnClick())) {
			a.setAttribute("onclick", result.getOnClick());
		}
		a.setText(result.getHeader());
		h3.addContent(a);
		
		Element p = new Element("p");
		p.setText(result.getPerex());
		div_sa_cc.addContent(p);
		
		Element div_sb_meta = new Element("div");
		div_sb_meta.setAttribute("class", "sb_meta");
		div_sa_cc.addContent(div_sb_meta);
		
		Element cite = new Element("cite");
		cite.setText(result.getShortUrl());
		div_sb_meta.addContent(cite);
		
		return li_sa_wr;
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
		response.getServicesHandle().getService(HtmlDomWriterService.class).setHTMLDom(responseDom);
	}

}
