package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;
import static au.org.intersect.exsite9.test.Assert.*;

import org.junit.Test;

/**
 * Tests {@link File}
 *
 * @author <a href="mailto:daniel@intersect.org.au">Daniel Yazbek</a>
 */
public final class FileUnitTest {

	@Test
	public void testConstruction() {
		final String f1 = "filename1";
		final String f2 = "filename2";

		final File toTest1 = new File(f1);
		final File toTest2 = new File(f1);
		final File toTest3 = new File(f2);

		assertEquals(toTest1, toTest1);
		assertEquals(f1, toTest1.getName());

		assertEquals(toTest1, toTest2);
		assertEquals(toTest2, toTest1);
		assertEquals(toTest1.hashCode(), toTest2.hashCode());

		assertNotEqualsHashCode(toTest2, toTest3);

		assertNotEquals(toTest1, null);
		assertNotEquals(toTest1, new Object());
		assertNotEquals(toTest1, f1);

		final String toString = toTest1.toString();
		assertTrue(toString.contains("name=" + f1));
	}
}
