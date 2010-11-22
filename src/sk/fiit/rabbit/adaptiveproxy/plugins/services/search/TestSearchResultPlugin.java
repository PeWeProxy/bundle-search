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
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;

public class TestSearchResultPlugin implements ResponseProcessingPlugin {

	@Override
	public void desiredResponseServices(
			Set<Class<? extends ProxyService>> desiredServices,
			ResponseHeader webRPHeader) {
		desiredServices.add(ModifiableSearchResultService.class);
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
		
		ModifiableSearchResultService modifiableSearchResultService = response.getServicesHandle().getService(ModifiableSearchResultService.class);
		modifiableSearchResultService.deleteResult(3);
		modifiableSearchResultService.moveResult(2, 7);
		modifiableSearchResultService.swapResults(3, 4);
		modifiableSearchResultService.putResult(new SearchResultObject("http://iwan.yweb.sk", "O slonoch", "Niekedy sa pozriem na nebo a spýtam sa „Kto som“? A vždy si uvedomím, že by to nebola taká sranda, keby som to vedel. Preto", 1));
		
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
