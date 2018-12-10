package de.heidelberg.collectionsexplorer;

import java.io.File;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class InputParameters {
	
	@Option(names = { "-v", "--verbose" }, description = "Be verbose.")
	private boolean verbose = false;
	
	@Parameters(arity = "1", paramLabel= "DIR", description="Input directory where the explorer will retrieve collections usage")
	private File inputDirectory;
	
}
