package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.heidelberg.collectionsexplorer.beans.StringListInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;
import de.heidelberg.collectionsexplorer.writer.CsvWriter;

@DisplayName("CSV Writer Test")
public class CSVWriterTest {

	private List<ObjectCreationInfo> entries = new ArrayList<>();
	
	@Test
	public void testObjectCreationInfoWritting() {
		
		@SuppressWarnings("serial")
		List<String> argumentTypes1 = new ArrayList<String>(){{ 
			add("Integer"); 
			add("String");
		}};
		
		ObjectCreationInfo entry1 = ObjectCreationInfo.builder().objectType("HashMap")
			.argumentTypes(new StringListInfo(argumentTypes1))
			.className("My Class")
			.arguments(new StringListInfo(new ArrayList<>()))
			.build();
		
		@SuppressWarnings("serial")
		ObjectCreationInfo entry2 = ObjectCreationInfo.builder().objectType("Integer")
				.argumentTypes(new StringListInfo(new ArrayList<String>(){{
					add("First");
					add("Second");
				}}))
				.className("My Class")
				.arguments(new StringListInfo(new ArrayList<String>() {{
					add("10");
				}})).build();
			
		entries.add(entry1);
		entries.add(entry2);
		
		try {
			File fileToWrite = new File("src/test/resources/de/heidelberg/collectionsexplorer/writable/test.csv");
			CsvWriter.writeInfo(fileToWrite , entries);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		
	}
	
}
