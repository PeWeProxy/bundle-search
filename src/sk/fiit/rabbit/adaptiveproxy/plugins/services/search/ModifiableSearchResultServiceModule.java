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
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomSenderService;

public class ModifiableSearchResultServiceModule implements ResponseServiceModule {

	private static final Logger logger = Logger.getLogger(ModifiableSearchResultServiceModule.class);
	
	@Override
	public boolean supportsReconfigure(PluginProperties newProps) {
		return false;
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
		desiredServices.add(HtmlDomSenderService.class);
	}

	@Override
	public void getProvidedResponseServices(
			Set<Class<? extends ProxyService>> providedServices) {
		providedServices.add(ModifiableSearchResultService.class);

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <Service extends ProxyService> ResponseServiceProvider<Service> provideResponseService(
			HttpResponse response, Class<Service> serviceClass)
			throws ServiceUnavailableException {
		
		if(ModifiableSearchResultService.class.equals(serviceClass)){
			Document responseDom = response.getServicesHandle().getService(HtmlDomBuilderService.class).getHTMLDom();
			String requestURI = response.getRequest().getRequestHeader().getRequestURI();
			
			if (GoogleSearchResultServiceProvider.isApplicableUrl(requestURI)) {
				return (ResponseServiceProvider<Service>) new GoogleModifiableSearchResultServiceProvider(responseDom);
			}
			else if (YahooSearchResultServiceProvider.isApplicableUrl(requestURI)) {
				return (ResponseServiceProvider<Service>) new YahooModifiableSearchResultServiceProvider(responseDom);
			}
			else if (BingSearchResultServiceProvider.isApplicableUrl(requestURI)){
				return (ResponseServiceProvider<Service>) new BingModifiableSearchResultServiceProvider(responseDom);
			}
		}
		return null;
	}

}
