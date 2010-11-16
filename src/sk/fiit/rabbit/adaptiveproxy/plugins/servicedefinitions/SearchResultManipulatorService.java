package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import sk.fiit.peweproxy.services.ProxyService;

public interface SearchResultManipulatorService extends ProxyService {
	public void putResult(int position, String url, String baseUrl, String title, String perex);
	public void moveResult(int positon, int newPosition);
	public void swapResults(int position1, int position2);
	public void deleteResult(int position);
}
