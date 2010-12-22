package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultObject;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultService;

public class BingSearchResultServiceProvider implements SearchResultService,
        ResponseServiceProvider<SearchResultService>{

	private static final String resultsParentElementPath = "/html/body/div[@id='sw_page']/div[@id='sw_width']/div[@id='sw_content']/div/div[@id='sw_canvas']/div[@id='sw_main']/div[@id='content']/div[@id='results_area']/div[@id='results_container']/div[@id='results']/ul";
	private static final String nameRelativePathInLi = "./div/div[@class='sb_tlst']/h3/a";
	private static final String perexRelativePathInLi = "./div/p";
	private static final String urlRelativePathInLi = "./div/div[@class='sb_meta']/cite";
	private static final String nameRelativeTaggedPathInLi = "./div/div[@class='ec_tr']/div[@class='nc_tc']/h3/a";
	private static final String perexRelativeTaggedPathInLi = "./div/div[@class='ec_tr']/div[@class='nc_mc']/p";
	private static final String urlRelativeTaggedPathInLi = "./div/div[@class='ec_tr']/div[@class='nc_mc']/div[@class='sb_meta']/span/cite";
	private static final String queryString = "/html/head/title";
	private static final Logger logger = Logger.getLogger(YahooSearchResultServiceProvider.class);
	
	private Document document;
	
	public BingSearchResultServiceProvider(Document document) {
	this.document=document;
	}
	
	@Override
	public SearchResultService getService() {
	return this;
	}
	
	@Override
	public boolean initChangedModel() {
	return true;
	}
	
	@Override
	public void doChanges(ModifiableHttpResponse response) {
	// do nothing
	
	}
	
	@Override
	public String getServiceIdentification() {
	return this.getClass().getName();
	}
	
	@Override
	public ArrayList<SearchResultObject> getSearchedData() {
	try {
		
		Element resultRoot = (Element)XPath.selectSingleNode(document.getRootElement(), resultsParentElementPath);
		ListIterator children = resultRoot.getChildren().listIterator();
		int counter = 0;
		ArrayList<SearchResultObject> finds = new ArrayList<SearchResultObject>();
		
		while(children.hasNext()){
			Element child = (Element) children.next();
			
			if(child.getAttribute("class").getValue().toString().compareTo("sa_wr")==0){
				Element name = (Element)XPath.selectSingleNode(child, nameRelativePathInLi);
				Element perex = (Element)XPath.selectSingleNode(child, perexRelativePathInLi);
				Element url = (Element)XPath.selectSingleNode(child, urlRelativePathInLi);
				counter++;
				finds.add(new SearchResultObject(url.getValue(), name.getValue(), perex.getValue(), counter));
			}
			else{
				Element name = (Element)XPath.selectSingleNode(child, nameRelativeTaggedPathInLi);
				Element perex = (Element)XPath.selectSingleNode(child, perexRelativeTaggedPathInLi);
				Element url = (Element)XPath.selectSingleNode(child, urlRelativeTaggedPathInLi);
				if(name!=null){
					counter++;
					finds.add(new SearchResultObject(url.getValue(), name.getValue(), perex.getValue(), counter));				
				}
			}
		}
		
		return finds;
		
	} catch (JDOMException e) {
		
	}
	
	return null;
	}
	
	@Override
	public String getQueryString() {
		try {
			Element resultRoot = (Element)XPath.selectSingleNode(document.getRootElement(), queryString);
			return this.splitIntoQueryString(resultRoot.getValue());
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String splitIntoQueryString(String title){
		String[] splitted = title.split("-");
		String queryString = "";
		
		for(int i=0; i<splitted.length-1; i++)
		{
			if(i==0)
				queryString = splitted[i];
			else
				queryString = queryString+"-"+splitted[i];
		}
		
		return queryString;
	}

}

