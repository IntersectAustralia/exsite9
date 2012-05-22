package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.assertNotEqualsHashCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests {@link Project}
 *
 * @author <a href="mailto:daniel@intersect.org.au">Daniel Yazbek</a>
 */
public final class ProjectUnitTest {

	@Test
	public void testConstruction() {
		final String n1 = "project 1";

		final Project toTest1 = new Project(n1);

		assertEquals(n1, toTest1.getName());
		assertTrue(toTest1.getGroups().isEmpty());
		assertTrue(toTest1.getFiles().isEmpty());

		final String toString = toTest1.toString();
		assertEquals("{name=group 1, nodes=[], files=[]}", toString);
	}

	@Test
	public void testEqualsHashCode() {
		final String n1 = "project 1";
		final String n2 = "project 2";

		final Project toTest1 = new Project(n1);
		final Project toTest2 = new Project(n1);
		final Project toTest3 = new Project(n2);

		assertEquals(toTest1, toTest1);

		assertEquals(toTest1, toTest2);
		assertEquals(toTest2, toTest1);
		assertEquals(toTest1.hashCode(), toTest2.hashCode());

		// Different name
		assertNotEqualsHashCode(toTest1, toTest3);
		assertNotEqualsHashCode(toTest2, toTest3);

		// Different child nodes.
		toTest1.getGroups().add(new Group("some group"));
		assertNotEqualsHashCode(toTest1, toTest2);
		assertNotEqualsHashCode(toTest1, toTest3);

		// Different child files.
		toTest1.getFiles().add(new File("some File"));
		assertNotEqualsHashCode(toTest1, toTest2);
		assertNotEqualsHashCode(toTest1, toTest3);
	}
}
