package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

import org.w3c.dom.Document;

import sk.fiit.peweproxy.services.ProxyService;

public interface SearchService extends ProxyService {
	public Document getHTMLDom();

}
