package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.SearchResultObject;

public interface SearchResultService extends ProxyService{
	
	public SearchResultObject[] getSearchedData();
	public String getQueryString();
}
