package de.heidelberg.collectionsexplorer.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvWriter {

	public static <T> void writeInfo(File toWrite, List<T> infoList) throws Exception {

		try (Writer writer = new FileWriter(toWrite)) {
			StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(writer)
					.withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();

			sbc.write(infoList);
		}
	}

}
