package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomSenderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.DeleteInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.Instruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.MoveInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.PutInstruction;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions.SwapInstruction;

public class GoogleModifiableSearchResultServiceProvider implements ModifiableSearchResultServiceProvider {

	private static final String resultsParentElementPath = "/html/body/div[@id='cnt']/div[@id='nr_container']/div[@id='center_col']/div[@id='res']/div[@id='ires']/ol";
	private static final Logger logger = Logger.getLogger(GoogleModifiableSearchResultServiceProvider.class);
	
	private Document responseDom;
	private ArrayList<Instruction> instructions;
	
	public GoogleModifiableSearchResultServiceProvider(Document responseDom) {
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
		GoogleSearchResultServiceProvider readingProvider = new GoogleSearchResultServiceProvider(responseDom); 
		return readingProvider.getSearchedData();
	}

	@Override
	public String getQueryString() {
		GoogleSearchResultServiceProvider readingProvider = new GoogleSearchResultServiceProvider(responseDom);
		return readingProvider.getQueryString();
	}
	
	@Override
	public String getResultsParentElementPath(){
		return GoogleModifiableSearchResultServiceProvider.resultsParentElementPath;
	}
	
	@Override
	public Document getResponseDom() {
		return responseDom;
	}

	@Override
	public Element puzzleResultElement(SearchResultObject result) {
		
		Element li_g = new Element("li");
		li_g.setAttribute("class", "g");
		
		Element div_vsc = new Element("div");
		div_vsc.setAttribute("class", "vsc");
		li_g.addContent(div_vsc);
		
		Element span_tl = new Element("span");
		span_tl.setAttribute("class", "tl");
		div_vsc.addContent(span_tl);
		
		Element h3_r = new Element("h3");
		h3_r.setAttribute("class", "r");
		span_tl.addContent(h3_r);
		
		Element a_url = new Element("a");
		a_url.setAttribute("href", result.getUrl());
		a_url.setText(result.getHeader());
		h3_r.addContent(a_url);
		
		Element div_s = new Element("div");
		div_s.setAttribute("class", "s");
		div_s.setText(result.getPerex());
		div_vsc.addContent(div_s);
		
		Element b = new Element("b");
		b.setText("...");
		div_s.addContent(b);
		
		Element br = new Element("br");
		div_s.addContent(br);
		
		Element span_f = new Element("span");
		span_f.setAttribute("class", "f");
		div_s.addContent(span_f);
		
		Element cite = new Element("cite");
		cite.setText(result.getShortUrl() + "/");
		span_f.addContent(cite);
		
		return li_g;
	}

	@Override
	public String getServiceIdentification() {
		return this.getClass().getName();
	}

	@Override
	public ModifiableSearchResultServiceProvider getService() {
		return this;
	}

	@Override
	public boolean initChangedModel() {
		return true;
	}

	@Override
	public void doChanges(ModifiableHttpResponse response) {
		for (Instruction instruction : instructions){
			instruction.execute((GoogleModifiableSearchResultServiceProvider)this);
		}
		response.getServicesHandle().getService(HtmlDomSenderService.class).setHTMLDom(responseDom);
	}

}
