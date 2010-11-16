package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import java.io.IOException;

import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.searchEngineService.SearchResultObject;

public interface SearchEngineService extends ProxyService{
	
	public SearchResultObject[] getSearchedData();
	public String getQueryString();
}
