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
		
		if(response.getServicesHandle().isServiceAvailable(ModifiableSearchResultService.class)){
			ModifiableSearchResultService modifiableSearchResultService = response.getServicesHandle().getService(ModifiableSearchResultService.class);
			String querryString = modifiableSearchResultService.getQueryString();
			querryString = querryString.trim();
			
			if ("catfight".equalsIgnoreCase(querryString)||"mačky".equalsIgnoreCase(querryString)){
				SearchResultObject searchResultObject = new SearchResultObject(
						"http://labss2.fiit.stuba.sk/TeamProject/2010/team17is-si/",
						"CAT fight",
						"Sme tím číslo 17. Túto stránku sme vytvorili za účelom prezentácie nášho úsilia počas dvoch semestrov na predmete Tímový projekt. Nájdete tu informácie",
						1);
				modifiableSearchResultService.putResult(searchResultObject);
			}
		}
		
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
