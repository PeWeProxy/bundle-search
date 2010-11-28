package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import org.jdom.Document;

import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultService;

public interface ModifiableSearchResultServiceProvider extends
		ModifiableSearchResultService,
		ResponseServiceProvider<SearchResultService> {
	
	public String getResultsParentElementPath();

	public Document getResponseDom();
}
