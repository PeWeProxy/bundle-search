package sk.fiit.rabbit.adaptiveproxy.plugins.services.search;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jdom.Document;
import org.jdom.Element;

import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.plugins.services.ResponseServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.SearchResultService;

public class GoogleSearchResultServiceProvider implements SearchResultService,
		ResponseServiceProvider<SearchResultService> {

	private Document document;

	public GoogleSearchResultServiceProvider(Document document) {
		this.document = document;
	}

	@Override
	public GoogleSearchResultServiceProvider getService() {
		return this;
	}

	@Override
	public boolean initChangedModel() {
		return true;
	}

	@Override
	public void doChanges(ModifiableHttpResponse response) {
		// do nothing
	}

	@Override
	public String getServiceIdentification() {
		return this.getClass().getName();
	}

	@Override
	public ArrayList<SearchResultObject> getSearchedData() {
		boolean foundP = false;
		boolean complete = false;
		int order = 0;
		String url = "";
		String header = "";
		String perex = "";
		ArrayList<SearchResultObject> result = new ArrayList<SearchResultObject>();

		Element elem = document.getRootElement().getChild("body");

		ListIterator children = elem.getChildren().listIterator();

		while (children.hasNext()) {
			Element child = (Element) children.next();

			if (child.getName().compareTo("p") == 0) {
				header = child.getValue();
				foundP = true;
			} else if (child.getName().compareTo("blockquote") == 0) {
				header = child.getChild("p").getValue();
				foundP = true;

				String[] splitted = splitString(child.getChild("table")
						.getValue());
				perex = splitted[0];
				url = splitted[1];
				foundP = false;
				complete = true;
			}

			if (child.getName().compareTo("table") == 0 && foundP == true) {
				String[] splitted = splitString(child.getValue());
				perex = splitted[0];
				url = splitted[1];
				foundP = false;
				complete = true;
			}

			if (complete) {
				order++;
				result.add(new SearchResultObject(url, header, perex, order));
				complete = false;
				url = "";
				header = "";
				perex = "";
			}
		}

		return result;
	}

	private String[] splitString(String string) {
		String[] splitted = string.split("-");
		String word = "";
		for (int i = 0; i <= splitted.length - 4; i++) {
			if (word.compareTo("") != 0)
				word = word + "-" + splitted[i];
			else
				word = word + splitted[i];
		}

		splitted = word.split("http:");
		word = "";
		if (splitted.length > 2)
			for (int i = 0; i <= splitted.length - 2; i++) {
				if (word.compareTo("") != 0)
					word = word + "http:" + splitted[i];
				else
					word = word + splitted[i];
			}
		else
			word = splitted[0];

		String[] back = new String[2];
		back[0] = word;
		if (splitted.length < 2)
			back[1] = "";
		else
			back[1] = "http:" + splitted[1];

		return back;
	}

	@Override
	public String getQueryString() {
		Element elem = document.getRootElement().getChild("head")
				.getChild("title");

		return elem.getValue().split("-")[0];
	}

}
