package de.heidelberg.collectionsexplorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyCounter {
	
	private List<CollectionsInfo> collInfo;
	
	public FrequencyCounter(List<CollectionsInfo> collInfo) {
		super();
		this.collInfo = collInfo;
	}

	public Map<String, Integer> countFrequencyByHeldType() {
		
		Map<String, Integer> heldTypeCounter = new HashMap<String, Integer>();
		for(CollectionsInfo coll : collInfo) {
			String objectHeld = coll.getObjectHeld();
			// increment in case of existing type
			Integer integer = heldTypeCounter.get(objectHeld);
			if(integer == null)
				heldTypeCounter.put(objectHeld, 1);
			else
				heldTypeCounter.put(objectHeld, integer + 1);
		}
		return heldTypeCounter;
	}
}
