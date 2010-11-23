package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomSenderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultService;

class GoogleModifiableSearchResultServiceProvider implements
		ModifiableSearchResultService, ResponseServiceProvider<SearchResultService> {
	
	private static final String resultsParentElementPath = "/html/body/div[@id='cnt']/div[@id='nr_container']/div[@id='center_col']/div[@id='res']/div[@id='ires']/ol";
	private static final Logger logger = Logger.getLogger(GoogleModifiableSearchResultServiceProvider.class);
	
	private Document responseDom;
	private ArrayList<Instruction> instructions;
	
	public GoogleModifiableSearchResultServiceProvider(Document responseDom) {
		this.responseDom = responseDom;
		this.instructions = new ArrayList<GoogleModifiableSearchResultServiceProvider.Instruction>(10);
	}
	
	private interface Instruction {
		public void execute(); 
	}
	
	private class PutInstruction implements Instruction{
		private int position;
		private String url;
		private String shortUrl;
		private String title;
		private String perex;
		
		public PutInstruction(SearchResultObject result) {
			this.position = result.getOrder();
			this.url = result.getUrl();
			this.shortUrl = result.getShortUrl();
			this.title = result.getHeader();
			this.perex = result.getPerex();
		}

		@Override
		public void execute() {
			System.out.println("Putting to " + position);
			//puzzle the result
			
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
			a_url.setAttribute("href", url);
			a_url.setText(title);
			h3_r.addContent(a_url);
			
			Element div_s = new Element("div");
			div_s.setAttribute("class", "s");
			div_s.setText(perex);
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
			cite.setText(shortUrl + "/");
			span_f.addContent(cite);

			//insert the result into document
			try {
				Element ol = (Element)XPath.selectSingleNode(responseDom.getRootElement(), resultsParentElementPath);
				ol.addContent(position, li_g);
			} catch (Exception e) {
				logger.error("Cannot put result", e);
			}
		}	
	}
	
	private class MoveInstruction implements Instruction{
		private int position;
		private int newPosition;
		
		public MoveInstruction(int position, int newPosition) {
			this.position = position;
			if(newPosition > position)
				this.newPosition = newPosition - 1; //because the result will be detached from original position
			else
				this.newPosition = newPosition;
		}

		@Override
		public void execute() {
			System.out.println("moving from " + position + " to " + newPosition);
			if (position == newPosition) return;
			try {
				Element result = (Element)XPath.selectSingleNode(responseDom.getRootElement(), resultsParentElementPath + "/li[" + position + "]");
				Element parent = result.getParentElement();
				
				result.detach();
				parent.addContent(newPosition, result);
			} catch (Exception e) {
				logger.error("Cannot move result", e);
			}
		}
	}
	
	private class SwapInstruction implements Instruction{
		private int position1;
		private int position2;
		
		public SwapInstruction(int position1, int position2) {
			this.position1 = (position1 < position2) ? position1 : position2; //smaller one
			this.position2 = (position2 > position1) ? position2 : position1; //larger one
		}

		@Override
		public void execute() {
			System.out.println("swapping " + position1 + ", " + position2);
			if (position1 == position2) return;
			try {
				Element result1 = (Element)XPath.selectSingleNode(responseDom.getRootElement(), resultsParentElementPath + "/li[" + position1 + "]");
				Element result2 = (Element)XPath.selectSingleNode(responseDom.getRootElement(), resultsParentElementPath + "/li[" + position2 + "]");
				Element parent = result1.getParentElement();
				
				result1.detach();
				result2.detach();
				parent.addContent(position1, result2);
				parent.addContent(position2, result1);
			} catch (Exception e) {
				logger.error("Cannot swap results", e);
			}			
		}	
	}
	
	private class DeleteInstruction implements Instruction{
		private int position;
		
		public DeleteInstruction(int position){
			this.position = position;
		}
		
		@Override
		public void execute(){			
			System.out.println("deleting " + position);
			try {
				Element result = (Element)XPath.selectSingleNode(responseDom.getRootElement(), resultsParentElementPath + "/li[" + position + "]");
				if (result != null){
					result.detach();
				}
			} catch (Exception e) {
				logger.error("Cannot delete result", e);
			}
		}
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
		GoogleSearchResultServiceProvider readingProvider = new GoogleSearchResultServiceProvider(responseDom); 
		return readingProvider.getSearchedData();
	}

	@Override
	public String getQueryString() {
		GoogleSearchResultServiceProvider readingProvider = new GoogleSearchResultServiceProvider(responseDom);
		return readingProvider.getQueryString();
	}
	
	@Override
	public String getServiceIdentification() {
		return this.getClass().getName();
	}

	@Override
	public GoogleModifiableSearchResultServiceProvider getService() {
		return this;
	}

	@Override
	public boolean initChangedModel() {
		return true;
	}

	@Override
	public void doChanges(ModifiableHttpResponse response) {
		for (Instruction instruction : instructions){
			instruction.execute();
		}
		response.getServicesHandle().getService(HtmlDomSenderService.class).setHTMLDom(responseDom);
	}

}
