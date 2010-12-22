package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import sk.fiit.peweproxy.services.ProxyService;

public interface ModifiableSearchResultService extends SearchResultService, ProxyService {
	public void putResult(SearchResultObject result);
	public void moveResult(int positon, int newPosition);
	public void swapResults(int position1, int position2);
	public void deleteResult(int position);
}
