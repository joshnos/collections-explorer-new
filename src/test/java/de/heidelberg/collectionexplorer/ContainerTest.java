package de.heidelberg.collectionexplorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ContainerTest {
	ArrayList<String> someArrayList = new ArrayList<String>();
	LinkedList<Integer> someLinkedList = new LinkedList<Integer>();
	Collection<Integer> someCollection = new ArrayList<Integer>();
	Map<String, String> someMap = new HashMap<String, String>();
	Map<Integer, String> anotherMap = new HashMap<Integer, String>();
	TreeMap<String, String> treeMap = new TreeMap<String, String>();
	Set<String> someHashSet = new HashSet<String>();
	private static String someString;
	private static ArrayList<Float> crunchyList;

	public static void someFunction() {
		someString = new String("Hello World");
		crunchyList = new ArrayList<Float>();
		int number = 5 + 5;
		System.out.println(someString + number + crunchyList);
	}
	
	
}
