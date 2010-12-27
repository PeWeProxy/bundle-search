package sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions;

public class SearchResultObject 
{
	private static final int maxUrlLength = 100; 
	
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
	
	public SearchResultObject(String url, String header, String perex)
	{
		this.url=url;
		this.header=header;
		this.perex=perex;
		this.order = -1;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public String getShortUrl()
	{
		if (this.url.length() > maxUrlLength){
			//TODO: invent nicer algorithm
			return this.url.substring(0, maxUrlLength - 1);
		}
		else{
			return this.url;
		}
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
