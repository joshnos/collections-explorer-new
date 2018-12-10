package de.heidelberg.collectionexplorer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class will take a Report and then use it to compile usage statistics.
 * Specifically, it will compile how often certain types of container contents are used.
 * 
 * @author Janos Sebök
 *
 */
public class ReportCruncher {
	
	static StringBuilder crunched_report = new StringBuilder();

	/**
	 * Helper method that converts a list into an occurrence map.
	 * TODO: find a way to make this work without using <Object>
	 * 
	 * @param list		a list that contains Strings in our case.
	 * @return			a map that has Strings as its keys and Integers as its values
	 * 					listing how often those Strings occurred in the given List
	 */
	 private static Map<Object, Integer> countOccurrences(List<Object> list) {
	      Map<Object, Integer> occurrenceMap = new HashMap<Object, Integer>();
	      for(Object obj: list){
	           Integer numOccurrence = occurrenceMap.get(obj);
	           if(numOccurrence == null)
	                occurrenceMap.put(obj, 1);
	           else
	                occurrenceMap.put(obj, occurrenceMap.get(obj) + 1);
	      }
	      return occurrenceMap;
	 }
	
	 /**
	  * Another helper method, this one sorts a Map by its values,
	  * in order to help us make a report sorted by the number of finds.
	  * TODO: find a way to make this work without using <Object>
	  * 
	  * @param map		a map that isn't sorted yet
	  * @return			a map that's sorted by value, in descending order
	  */
	 public static Map<Object, Integer> sortByValue (Map<Object, Integer> map) {
		 Map<Object, Integer> sortedMap = new TreeMap<Object, Integer>(new ValueComparator(map));
		 sortedMap.putAll(map);
		 return sortedMap;
	 }
	 
	 /**
	  * Third helper method, this one adds two maps together. Specifically, it adds the
	  * first parameter map's values to the values with the same key in the second map,
	  * then returns that result, which is the second map at that point.
	  * TODO: find a way to make this work without using <Object>
	  * 
	  * @param map1		any <Object, Integer> map, here: <String, Integer>
	  * @param map2		see above
	  * @return			an <Object, Integer> map, in our case <String, Integer>,
	  * 				except this map contains all the keys in map1 and map2,
	  * 				and if there were duplicates, the new value is the sum.
	  */
	 private static Map<Object, Integer> addMaps(Map<Object, Integer> map1, Map<Object, Integer> map2) {
		 Set<Map.Entry<Object, Integer>> entries = map1.entrySet();
		 for (Map.Entry<Object, Integer> entry : entries) {
			 Integer map2v = map2.get(entry.getKey());
			 	if (map2v == null)
			 		map2.put(entry.getKey(), entry.getValue());
			 	else
			 		map2.put(entry.getKey(), entry.getValue() + map2v);
		 }
		 return map2;
	 }
	 
	 /**
	  * Another helper method, this one just returns the sum of all the values.
	  * 
	  * @param map		A map, with Integer values.
	  * @return			The sum of all the map's values added together.
	  */
	 private static int sumValues(Map<Object, Integer> map) {
		 int sum = 0;
		 for (Integer v : map.values())
			 sum += v;
		 return sum;
	 }
	 
	/**
	 * The main function this class provides.
	 * It will process a given Report and then create human-readable output
	 * in the shape of a String to the standard output.
	 * 
	 * TODO: separate crunch and print into two different functions
	 */
	public static void crunchAndPrint(Report r) {
		
		/**
		 * results is a List of various entries, where each one is a list in itself as well as a file name attached to it.
		 */
		List<Result> results = r.getResults();
		List<String> contents = new ArrayList<String>();
		List<CollectionInfoBean> beans = new ArrayList<CollectionInfoBean>();
		Map<Object, Integer> total_occurrences = new HashMap<Object, Integer>();
		
		for (Result res: results) {
			contents = new ArrayList<String>(); 		// need to make sure to create a new one every iteration
			if (res == null)
				continue;
			if (res.hasEntries())
				beans = res.getEntries();
			else
				continue;
			crunched_report.append("In file: " + res.getFilePath() + "\n");
			for (CollectionInfoBean bean: beans) {
				crunched_report.append(bean);
				if (bean.hasObjectHeld())
					for (String substring : bean.getObjectHeld().split(" "))
						contents.add(substring);
			}
			List<Object> objectList = new ArrayList<Object>(contents);
			Map<Object, Integer> file_occurrences = countOccurrences(objectList);
			file_occurrences = sortByValue(file_occurrences);
			total_occurrences = addMaps(file_occurrences, total_occurrences);
			int sum = sumValues(file_occurrences);
			printOccurrencePercentages(file_occurrences, sum);
		}
		total_occurrences = sortByValue(total_occurrences);
		crunched_report.append("Held containers across all files:");
		printOccurrencePercentages(total_occurrences, sumValues(total_occurrences));
		System.out.println(crunched_report);
	}


	/**
	 * This simply converts a map into human-readable occurrences along with percentages of a whole.
	 * TODO: remove redundancy
	 * 
	 * @param file_occurrences		an occurrence map
	 * @param sum					the sum of the values of that map, kind of redundant
	 */
	private static void printOccurrencePercentages(Map<Object, Integer> file_occurrences, int sum) {
		for (Entry<Object, Integer> entry : file_occurrences.entrySet()) {
			int value = entry.getValue();
			double percentage = value / (double)sum;
			percentage = Math.round(percentage * 10000d) / 100d;
		    String percentage_string = new String("\n" + entry.getKey() + " " + value + " " + percentage + "%");
			crunched_report.append(percentage_string);
		}
		crunched_report.append("\n");
	}

	@Override
	public String toString() {
		return crunched_report.toString();
	}
}
