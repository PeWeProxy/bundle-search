package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import java.util.ArrayList;

import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.SearchResultObject;

public interface SearchResultService extends ProxyService{
	
	public ArrayList<SearchResultObject> getSearchedData();
	public String getQueryString();
}
