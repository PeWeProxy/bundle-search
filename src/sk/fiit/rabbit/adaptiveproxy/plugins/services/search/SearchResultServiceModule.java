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
import sk.fiit.peweproxy.services.content.StringContentService;
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
		desiredServices.add(StringContentService.class);
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
			Document document = htmlDomBuilderService.getHTMLDom();
			
			return (ResponseServiceProvider<Service>) new GoogleSearchResultServiceProvider(document);
		}
		
		return null;
	}
	
}
