package de.heidelberg.collectionexplorer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class FrequencyCounterTest {

	@Test
	public void testFrequencyCounterWithZeroElements() throws Exception {
		
		List<CollectionInfoBean> list = new ArrayList<>();
		FrequencyCounter counter = new FrequencyCounter(list );
		
		Map<String, Integer> countFrequencyByHeldType = counter.countFrequencyByHeldType();
		assertNotNull(countFrequencyByHeldType);
		assertEquals(countFrequencyByHeldType.isEmpty(), true);
		
		
	}
	
	@Test
	public void testFrequencyCounterWithOneElement() throws Exception {
		
		List<CollectionInfoBean> list = new ArrayList<>();
		FrequencyCounter counter = new FrequencyCounter(list);
		
		CollectionInfoBean e = new CollectionInfoBean();
		e.setObjectHeld("String");
		list.add(e);
		
		Map<String, Integer> countFrequencyByHeldType = counter.countFrequencyByHeldType();
		assertNotNull(countFrequencyByHeldType);
		assertEquals(countFrequencyByHeldType.isEmpty(), false);
		assertEquals(countFrequencyByHeldType.get("String"), 1);
		
	}
	
	@Test
	public void testFrequencyCounterWithTwoElementWithSameType() throws Exception {
		
		List<CollectionInfoBean> list = new ArrayList<>();
		FrequencyCounter counter = new FrequencyCounter(list);
		
		CollectionInfoBean e = new CollectionInfoBean();
		e.setObjectHeld("String");
		list.add(e);
		
		CollectionInfoBean e2 = new CollectionInfoBean();
		e2.setObjectHeld("String");
		list.add(e2);
		
		Map<String, Integer> countFrequencyByHeldType = counter.countFrequencyByHeldType();
		assertNotNull(countFrequencyByHeldType);
		assertEquals(countFrequencyByHeldType.isEmpty(), false);
		assertEquals(countFrequencyByHeldType.get("String"), 2);
		
	}
	
	@Test
	public void testFrequencyCounterWithTwoElementWithDifferentType() throws Exception {
		
		List<CollectionInfoBean> list = new ArrayList<>();
		FrequencyCounter counter = new FrequencyCounter(list);
		
		CollectionInfoBean e = new CollectionInfoBean();
		e.setObjectHeld("String");
		list.add(e);
		
		CollectionInfoBean e2 = new CollectionInfoBean();
		e2.setObjectHeld("Integer");
		list.add(e2);
		
		Map<String, Integer> countFrequencyByHeldType = counter.countFrequencyByHeldType();
		assertNotNull(countFrequencyByHeldType);
		assertEquals(countFrequencyByHeldType.isEmpty(), false);
		assertEquals(countFrequencyByHeldType.get("String"), 1);
		assertEquals(countFrequencyByHeldType.get("Integer"), 1);
		assertEquals(countFrequencyByHeldType.size(), 2);
				
		
	}
	
}
