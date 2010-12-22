package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Document;

import sk.fiit.peweproxy.headers.ResponseHeader;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.plugins.PluginProperties;
import sk.fiit.peweproxy.plugins.services.ResponseServiceModule;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.peweproxy.services.ServiceUnavailableException;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomBuilderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultService;

public class SearchResultServiceModule implements ResponseServiceModule{
	
	private static final Logger logger = Logger.getLogger(SearchResultServiceModule.class);

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
		desiredServices.add(HtmlDomBuilderService.class);
	}

	@Override
	public void getProvidedResponseServices(
			Set<Class<? extends ProxyService>> providedServices) {
		providedServices.add(SearchResultService.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Service extends ProxyService> ResponseServiceProvider<Service> provideResponseService(
			HttpResponse response, Class<Service> serviceClass)
			throws ServiceUnavailableException {
		
		if(serviceClass.equals(SearchResultService.class)) {
			HtmlDomBuilderService htmlDomBuilderService = response.getServicesHandle().getService(HtmlDomBuilderService.class);
			Document document;
			String requestURI = response.getRequest().getRequestHeader().getRequestURI();
			if(isGoogleSearchResult(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new GoogleSearchResultServiceProvider(document);
			}
			if(isYahooSearchResult(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new YahooSearchResultServiceProvider(document);
			}
			if(isBingSearchResult(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new BingSearchResultServiceProvider(document);
			}		
		}
		
		return null;
	}
	
	private boolean isGoogleSearchResult(String requestURI) {
		if(requestURI.matches("http://www\\.google\\.[a-z]{2,4}/search\\?.+")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isYahooSearchResult(String requestURI){
		if(requestURI.matches("http://search\\.yahoo\\.com/search.+")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isBingSearchResult(String requestURI){
		if(requestURI.matches("http://www\\.bing\\.com/search.+")) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
