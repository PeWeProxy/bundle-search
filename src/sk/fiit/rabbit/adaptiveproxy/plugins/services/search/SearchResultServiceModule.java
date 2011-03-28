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
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomReaderService;
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
		desiredServices.add(HtmlDomReaderService.class);
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
			HtmlDomReaderService htmlDomBuilderService = response.getServicesHandle().getService(HtmlDomReaderService.class);
			Document document;
			String requestURI = response.getRequest().getRequestHeader().getRequestURI();
			if(GoogleSearchResultServiceProvider.isApplicableUrl(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new GoogleSearchResultServiceProvider(document);
			}
			if(YahooSearchResultServiceProvider.isApplicableUrl(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new YahooSearchResultServiceProvider(document);
			}
			if(BingSearchResultServiceProvider.isApplicableUrl(requestURI)){
				document = htmlDomBuilderService.getHTMLDom();
				return (ResponseServiceProvider<Service>) new BingSearchResultServiceProvider(document);
			}		
		}
		
		return null;
	}
	
}
