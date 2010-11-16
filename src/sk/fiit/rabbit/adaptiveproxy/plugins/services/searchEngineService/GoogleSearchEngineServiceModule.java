package sk.fiit.rabbit.adaptiveproxy.plugins.services.searchEngineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sk.fiit.peweproxy.headers.ResponseHeader;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.PluginProperties;
import sk.fiit.peweproxy.plugins.services.ResponseServiceModule;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.peweproxy.services.ServiceUnavailableException;
import sk.fiit.peweproxy.services.content.StringContentService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomBuilderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchEngineService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.htmldom.HtmlDomBuilderModule.HtmlDomBuilderProvider;

public class GoogleSearchEngineServiceModule implements ResponseServiceModule{
	
	private static final Logger logger = Logger.getLogger(GoogleSearchEngineServiceModule.class);

	public class GoogleSearchEngineProvider
	        implements SearchEngineService, ResponseServiceProvider<GoogleSearchEngineProvider>{
		
		private Document document;
		
		public GoogleSearchEngineProvider(Document document) {
			this.document = document;
		}

		@Override
		public GoogleSearchEngineProvider getService() {
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
		public SearchResultObject[] getSearchedData() {
			boolean foundP = false;
			boolean complete = false;
			int order=0;
			String url="";
			String header="";
			String perex="";
			ArrayList<SearchResultObject> result = new ArrayList<SearchResultObject>();
			
		    Element elem = document.getRootElement().getChild("body");	
		    
			ListIterator children = elem.getChildren().listIterator();
			
			while(children.hasNext())
			{	
				Element child = (Element)children.next();
				
				if(child.getName().compareTo("p")==0)
				{
					header=child.getValue();
					foundP=true;
				}
				else if(child.getName().compareTo("blockquote")==0)
				{
					header= child.getChild("p").getValue();
					foundP=true;
					
					String[] splitted = splitString(child.getChild("table").getValue());
					perex=splitted[0];
					url=splitted[1];
					foundP=false;
					complete=true;
				}
				
				if(child.getName().compareTo("table")==0 && foundP==true)
				{
					String[] splitted = splitString(child.getValue());
					perex=splitted[0];
					url=splitted[1];
					foundP=false;
					complete=true;
				}
				
				if(complete)
				{
					order++;
					result.add(new SearchResultObject(url, header, perex, order));
					complete=false;
					url="";
					header="";
					perex="";
				}
			}
			
			SearchResultObject[] results=new SearchResultObject[result.size()];
			for(int i=0; i<results.length; i++)
				results[i]=result.get(i);
			
			return results;
		}
		
		private String[] splitString(String string)
		{
			String[] splitted = string.split("-");
			String word = "";
			for(int i=0; i<=splitted.length-4; i++)
			{
				if(word.compareTo("")!=0)
					word=word+"-"+splitted[i];
				else
					word=word+splitted[i];
			}
			
			splitted = word.split("http:");
			word="";
			if(splitted.length>2)
				for(int i=0; i<=splitted.length-2; i++)
				{
					if(word.compareTo("")!=0)
						word = word + "http:" + splitted[i];
					else
						word = word + splitted[i];
				}
			else
				word = splitted[0];
			
			String[] back = new String[2];
			back[0]=word;
			if(splitted.length<2)
				back[1]="";
			else
				back[1]="http:"+splitted[1];
			
			return back;
		}

		@Override
		public String getQueryString() {
			Element elem = document.getRootElement().getChild("head").getChild("title");
			
			return elem.getValue().split("-")[0];
		}
		
	}

	@Override
	public boolean supportsReconfigure(PluginProperties newProps) {
		return true;
	}

	@Override
	public boolean start(PluginProperties props) {
		return true;
	}

	@Override
	public void stop() {
	}

	@Override
	public void desiredResponseServices(
			Set<Class<? extends ProxyService>> desiredServices,
			ResponseHeader webRPHeader) {
		desiredServices.add(StringContentService.class);
	}

	@Override
	public void getProvidedResponseServices(
			Set<Class<? extends ProxyService>> providedServices) {
		providedServices.add(SearchEngineService.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Service extends ProxyService> ResponseServiceProvider<Service> provideResponseService(
			HttpResponse response, Class<Service> serviceClass)
			throws ServiceUnavailableException {
		
		if(serviceClass.equals(SearchEngineService.class)) {
			HtmlDomBuilderService htmlDomBuilderService = response.getServicesHandle().getService(HtmlDomBuilderService.class);
			Document document = htmlDomBuilderService.getHTMLDom();
			
			return (ResponseServiceProvider<Service>) new GoogleSearchEngineProvider(document);
		}
		
		return null;
	}
	
}
