package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.BingModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.SearchResultObject;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

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

	@Override
	public void execute(YahooModifiableSearchResultServiceProvider provider) {
		responseDom = provider.getResponseDom();
		
		System.out.println("Putting to " + position);
		//puzzle the result
		
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
		a_yschttl_spt.setAttribute("href", url);
		a_yschttl_spt.setText(title);
		h3.addContent(a_yschttl_spt);
		
		Element div_abstr = new Element("div");
		div_abstr.setAttribute("class", "abstr");
		div_abstr.setText(perex);
		div_res.addContent(div_abstr);
		
		Element span_url = new Element("span");
		span_url.setAttribute("class", "url");
		span_url.setText(shortUrl);
		div_res.addContent(span_url);
		
		//insert the result into document
		try {
			Element ol = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ol.addContent(position - 1, li); //works better for Yahoo! with minus one 
		} catch (Exception e) {
			//TODO: logger???
			//logger.error("Cannot put result", e);
		}
		
	}

	@Override
	public void execute(BingModifiableSearchResultServiceProvider provider) {
		responseDom = provider.getResponseDom();
		
		System.out.println("Putting to " + position);
		//puzzle the result	
		
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
		a.setAttribute("href", url);
		a.setText(title);
		h3.addContent(a);
		
		Element p = new Element("p");
		p.setText(perex);
		div_sa_cc.addContent(p);
		
		Element div_sb_meta = new Element("div");
		div_sb_meta.setAttribute("class", "sb_meta");
		div_sa_cc.addContent(div_sb_meta);
		
		Element cite = new Element("cite");
		cite.setText(shortUrl);
		div_sb_meta.addContent(cite);
		
		//insert the result into document
		try {
			Element ul = (Element)XPath.selectSingleNode(responseDom.getRootElement(), provider.getResultsParentElementPath());
			ul.addContent(position - 1, li_sa_wr); //works better for Bing with minus one
		} catch (Exception e) {
			//TODO: logger???
			//logger.error("Cannot put result", e);
		}
	}

}
