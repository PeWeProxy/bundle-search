package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.Set;

import sk.fiit.peweproxy.headers.ResponseHeader;
import sk.fiit.peweproxy.messages.HttpMessageFactory;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.PluginProperties;
import sk.fiit.peweproxy.plugins.processing.ResponseProcessingPlugin;
import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomBuilderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.HtmlDomSenderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultManipulatorService;

public class SearchResultManipulatorPlugin implements ResponseProcessingPlugin {

	@Override
	public void desiredResponseServices(
			Set<Class<? extends ProxyService>> desiredServices,
			ResponseHeader webRPHeader) {
		desiredServices.add(SearchResultManipulatorService.class);
		desiredServices.add(HtmlDomBuilderService.class);
		desiredServices.add(HtmlDomSenderService.class);
	}

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
	public ResponseProcessingActions processResponse(
			ModifiableHttpResponse response) {
		
		SearchResultManipulatorService searchResultManipulatorService = response.getServicesHandle().getService(SearchResultManipulatorService.class);
		searchResultManipulatorService.deleteResult(3);
		searchResultManipulatorService.moveResult(2, 7);
		searchResultManipulatorService.swapResults(3, 4);
		searchResultManipulatorService.putResult(1, "iwan.yweb.sk", "iwan.yweb.sk", "O slonoch", "Niekedy sa pozriem na nebo a spýtam sa „Kto som“? A vždy si uvedomím, že by to nebola taká sranda, keby som to vedel. Preto");
		
		return ResponseProcessingActions.PROCEED;
	}

	@Override
	public HttpResponse getNewResponse(ModifiableHttpResponse response,
			HttpMessageFactory messageFactory) {
		return null;
	}

	@Override
	public void processTransferedResponse(HttpResponse response) {

	}

}
