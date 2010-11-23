package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.GoogleModifiableSearchResultServiceProvider;

public interface Instruction {
	//TODO: refactor
	public void execute(GoogleModifiableSearchResultServiceProvider provider);
}
