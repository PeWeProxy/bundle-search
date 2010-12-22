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

public class YahooSearchResultServiceProvider implements SearchResultService,
        ResponseServiceProvider<SearchResultService>{
	
	private static final String resultsParentElementPath = "/html/body/div[@id='doc']/div[@id='bd']/div[@id='results']/div[@id='cols']/div[@id='left']/div[@id='main']/div[@id='web']/ol";
	private static final String nameRelativePathInLi = "./div[@class='res']/div/h3/a";
	private static final String nameRelativeTaggedPathInLi = "./div/div/h3/a";
	private static final String perexRelativePathInLi = "./div[@class='res']/div[@class='abstr']";
	private static final String perexRelativeTaggedPathInLi = "./div/div/div/div";
	private static final String urlRelativePathInLi = "./div[@class='res']/span[@class='url']";
	private static final String urlRelativeTaggedPathInLi = "./div/div/div/div/span";
	private static final String queryString = "/html/head/title";
	private static final Logger logger = Logger.getLogger(YahooSearchResultServiceProvider.class);
	
	private Document document;
	
	public YahooSearchResultServiceProvider(Document document) {
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
				
				if(child.getChild("div").getAttribute("class").getValue().toString().compareTo("res")==0){
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
