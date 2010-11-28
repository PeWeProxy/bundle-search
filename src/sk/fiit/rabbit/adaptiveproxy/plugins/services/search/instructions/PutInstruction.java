package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.SearchResultObject;

public class PutInstruction implements Instruction {
	
	private int position;
	private String url;
	private String shortUrl;
	private String title;
	private String perex;
	
	private Document responseDom;
	
	public PutInstruction(SearchResultObject result) {
		this.position = result.getOrder();
		this.url = result.getUrl();
		this.shortUrl = result.getShortUrl();
		this.title = result.getHeader();
		this.perex = result.getPerex();
	}
	
	@Override
	public void execute(ModifiableSearchResultServiceProvider provider) {
		// TODO Auto-generated method stub
		
	}

	public void execute(GoogleModifiableSearchResultServiceProvider provider) {
		responseDom = provider.getResponseDom();
		
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
			Element ol = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ol.addContent(position, li_g);
		} catch (Exception e) {
			//TODO: logger???
			//logger.error("Cannot put result", e);
		}
	}

}
