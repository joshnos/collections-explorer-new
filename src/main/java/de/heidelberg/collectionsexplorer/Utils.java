package de.heidelberg.collectionsexplorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<String> readLineByLine(File file) throws FileNotFoundException, IOException {
		// Reading file
		BufferedReader br = new BufferedReader(new FileReader(file));
		br.readLine(); // Get Array Size

		List<String> lines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();	
		return lines;
	}

	public static void writeFile(StringBuilder content, File outputFile) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		writer.println(content);
		writer.close();	
	}
}
