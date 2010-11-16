package sk.fiit.rabbit.adaptiveproxy.plugins.services.searchEngineService;

public class SearchResultObject 
{
	private String url;
	private String header;
	private String perex;
	private int order;
	
	public SearchResultObject(String url, String header, String perex, int order)
	{
		this.url=url;
		this.header=header;
		this.perex=perex;
		this.order=order;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public String getHeader()
	{
		return this.header;
	}
	
	public String getPerex()
	{
		return this.perex;
	}
	
	public int getOrder()
	{
		return this.order;
	}
}
