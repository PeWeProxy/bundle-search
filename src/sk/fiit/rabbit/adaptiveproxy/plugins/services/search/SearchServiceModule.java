package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.dom4j.DocumentException;

import com.dappit.Dapper.parser.MozillaParser;
import com.dappit.Dapper.parser.ParserException;
import com.dappit.Dapper.parser.ParserInitializationException;

import sk.fiit.peweproxy.headers.ResponseHeader;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.PluginProperties;
import sk.fiit.peweproxy.plugins.services.ResponseServiceModule;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.peweproxy.services.ServiceUnavailableException;
import sk.fiit.peweproxy.services.content.StringContentService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchService;

public class SearchServiceModule implements ResponseServiceModule {
	
	private static final Logger logger = Logger.getLogger(SearchServiceModule.class);
	
	private class SearchServiceProvider
			implements SearchService, ResponseServiceProvider<SearchServiceProvider> {

		private String content;
		
		public SearchServiceProvider(String content) {
			this.content = content;
		}
		
		@Override
		public Document getHTMLDom() {
			Document document = null;
			try {
				MozillaParser.init();
				MozillaParser parser = new MozillaParser();
				document = parser.parse(content);
			} catch (DocumentException e) {
				logger.error("Dom4j document exception", e);
			} catch (ParserConfigurationException e) {
				logger.error("Mozilla parser configuration exception", e);
			} catch (ParserInitializationException e) {
				logger.error("Mozilla pareser initialization exception", e);
			} catch (ParserException e) {
				logger.error("Mozilla pareser exception", e);
			}
			return document;
		}


		@Override
		public String getServiceIdentification() {
			return this.getClass().getName();
		}

		@Override
		public SearchServiceProvider getService() {
			return this;
		}

		@Override
		public boolean initChangedModel() {
			return false;
		}

		@Override
		public void doChanges(ModifiableHttpResponse response) {
			// this service makes no modifications
		}

	}

	@Override
	public boolean supportsReconfigure(PluginProperties newProps) {
		return true;
	}

	@Override
	public boolean start(PluginProperties props) {
		return true;
	}

	@Override
	public void stop() {
	}

	@Override
	public void desiredResponseServices(
			Set<Class<? extends ProxyService>> desiredServices,
			ResponseHeader webRPHeader) {
		desiredServices.add(StringContentService.class);
	}

	@Override
	public void getProvidedResponseServices(
			Set<Class<? extends ProxyService>> providedServices) {
		providedServices.add(SearchService.class);
	}

	@Override
	public <Service extends ProxyService> ResponseServiceProvider<Service> provideResponseService(
			HttpResponse response, Class<Service> serviceClass)
			throws ServiceUnavailableException {
		
		if(serviceClass.equals(SearchService.class)) {
			String content = response.getServicesHandle().getService(StringContentService.class).getContent();
			return (ResponseServiceProvider<Service>) new SearchServiceProvider(content);
		}
		
		return null;
	}

	public static void main(String[] args) {
		String content = "<html><body>";
			for (int i = 0; i < 100; i++)
				content += "<li><table><tr><td>222222222222222</table>\n";
			content += "</body></html>";
		
		Document document = null;
		try {
			MozillaParser.init();
			MozillaParser parser = new MozillaParser();
			document = parser.parse(content);
		} catch (DocumentException e) {
			System.err.println("Dom4j document exception");
		} catch (ParserConfigurationException e) {
			System.err.println("Mozilla parser configuration exception");
		} catch (ParserInitializationException e) {
			System.err.println("Mozilla pareser initialization exception");
		} catch (ParserException e) {
			System.err.println("Mozilla pareser exception");
		} catch (Exception e) {
			System.err.println("Unknown Mozilla parser exception.");
		}		
		
		System.out.println(document.getXmlVersion());
		System.out.println(document.getDocumentElement().getTagName());
	}
}
