package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import java.util.ArrayList;

import sk.fiit.peweproxy.services.ProxyService;

public interface SearchResultService extends ProxyService{
	
	public ArrayList<SearchResultObject> getSearchedData();
	public String getQueryString();
}
