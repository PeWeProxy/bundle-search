package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import org.jdom.Document;
import org.jdom.Element;

import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.ModifiableSearchResultService;

public interface ModifiableSearchResultServiceProvider extends
		ModifiableSearchResultService,
		ResponseServiceProvider<ModifiableSearchResultService> {
	
	public String getResultsParentElementPath();

	public Document getResponseDom();
	
	public Element puzzleResultElement(SearchResultObject result);
}
