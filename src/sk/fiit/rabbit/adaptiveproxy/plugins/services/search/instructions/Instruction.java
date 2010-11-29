package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.YahooModifiableSearchResultServiceProvider;

public interface Instruction {
	public void execute(GoogleModifiableSearchResultServiceProvider provider);
	public void execute(YahooModifiableSearchResultServiceProvider provider);
}