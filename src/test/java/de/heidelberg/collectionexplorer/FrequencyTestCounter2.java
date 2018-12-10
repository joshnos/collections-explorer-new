package de.heidelberg.collectionexplorer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FrequencyTestCounter2 {

	@Test
	public void testZeroElements() throws Exception {

		List<CollectionInfoBean> collInfo = new ArrayList<>();

		FrequencyCounter counter = new FrequencyCounter(collInfo);

		Map<String, Integer> frequencyMap = counter.countFrequencyByHeldType();
		assertNotNull(frequencyMap);
		assertTrue(frequencyMap.isEmpty());

	}

	@Test
	public void testWithOneElement() throws Exception {

		List<CollectionInfoBean> collInfo = new ArrayList<>();
		
		CollectionInfoBean e = new CollectionInfoBean();
		e.setObjectHeld("String");
		collInfo.add(e);

		FrequencyCounter counter = new FrequencyCounter(collInfo);

		Map<String, Integer> frequencyMap = counter.countFrequencyByHeldType();
		assertNotNull(frequencyMap);
		assertFalse(frequencyMap.isEmpty());
		assertEquals(frequencyMap.size(), 1);

	}

}
