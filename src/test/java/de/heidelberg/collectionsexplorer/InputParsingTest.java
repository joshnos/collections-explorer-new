package de.heidelberg.collectionsexplorer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.heidelberg.collectionsexplorer.CollectionsExplorer;
import picocli.CommandLine;

@DisplayName("Testing the input parsing of CollectionsExplorer")
public class InputParsingTest {
	
	@Test
	public void testinInputParsing() {
		
		String[] args = {"-v", "Root/MyDirectory", "Root/MyReport"};
		
		CollectionsExplorer explorer = CommandLine.populateCommand(new CollectionsExplorer(), args);
		
		assertTrue(explorer.isVerbose());

		assertEquals(explorer.getInputDirectory().getPath(), "Root\\MyDirectory");
		assertEquals(explorer.getOutputFile().getPath(), "Root\\MyReport");
		
		
	}

}
