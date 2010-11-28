package sk.fiit.rabbit.adaptiveproxy.plugins.services.search.instructions;

import sk.fiit.rabbit.adaptiveproxy.plugins.services.search.ModifiableSearchResultServiceProvider;

public interface Instruction {
	public void execute(ModifiableSearchResultServiceProvider provider);
}