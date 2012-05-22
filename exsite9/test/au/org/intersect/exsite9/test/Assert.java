package au.org.intersect.exsite9.test;

import static org.junit.Assert.*;

/**
 * Some usefull methods to aid in unit testing. In addition to {@link junit.framework.Assert}
 *
 * @author <a href="mailto:daniel@intersect.org.au">Daniel Yazbek</a>
 */
public final class Assert {

	/**
	 * No instances please.
	 */
	private Assert() {
	}

	/**
	 * Asserts that the provided objects are not equal.
	 * @param obj1 The first object.
	 * @param obj2 The second object.
	 */
	public static void assertNotEquals(final Object obj1, final Object obj2) {
		assertFalse(obj1.equals(obj2));
	}

	/**
	 * Asserts that the provided objects are not equal AND have a different hash code.
	 * @param obj1 The first object.
	 * @param obj2 The second object.
	 */
	public static void assertNotEqualsHashCode(final Object obj1, final Object obj2) {
		assertNotEquals(obj1, obj2);
		assertFalse(obj1.hashCode() == obj2.hashCode());
	}
}
