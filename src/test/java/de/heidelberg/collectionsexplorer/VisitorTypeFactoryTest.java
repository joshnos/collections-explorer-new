package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.context.VisitorType;
import de.heidelberg.collectionsexplorer.visitors.ObjectCreationVisitor;

public class VisitorTypeFactoryTest {

	@Test
	public void testObjectCreation() {
		
		VoidVisitorAdapter<Result<ObjectCreationInfo>> instance = 
				VisitorType.OBJECT_CREATION.getInstance(new Filter(), null);
		
		assertNotNull(instance);
		assertTrue(instance instanceof ObjectCreationVisitor);
		
	}
	
}
