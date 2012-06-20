/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static au.org.intersect.exsite9.test.Assert.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link Pair}
 */
public final class PairUnitTest
{
    @Test
    public void testPair()
    {
        final String first = "first";
        final String second = "second";
        final Pair<String, String> toTest1 = new Pair<String, String>(first, second);

        assertEquals(first, toTest1.getFirst());
        assertEquals(second, toTest1.getSecond());

        assertTrue(toTest1.toString().contains("first=first"));
        assertTrue(toTest1.toString().contains("second=second"));
    }

    @Test
    public void testEqualsHashCode()
    {
        final String first = "first";
        final String second = "second";
        final Pair<String, String> toTest1 = new Pair<String, String>(first, second);
        final Pair<String, String> toTest2 = new Pair<String, String>(first, second);
        final Pair<String, String> toTest3 = new Pair<String, String>("someOther", second);
        final Pair<String, String> toTest4 = new Pair<String, String>(first, "someOther");
        final Pair<String, String> toTest5 = new Pair<String, String>("foo", "bar");

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertTrue(toTest1.hashCode() == toTest2.hashCode());

        assertNotEqualsHashCode(toTest2, toTest3);
        assertNotEqualsHashCode(toTest2, toTest4);
        assertNotEqualsHashCode(toTest2, toTest5);

        assertNotEqualsHashCode(toTest3, toTest2);
        assertNotEqualsHashCode(toTest3, toTest4);
        assertNotEqualsHashCode(toTest3, toTest5);

        assertNotEqualsHashCode(toTest4, toTest2);
        assertNotEqualsHashCode(toTest4, toTest3);
        assertNotEqualsHashCode(toTest4, toTest5);

        assertNotEqualsHashCode(toTest5, toTest2);
        assertNotEqualsHashCode(toTest5, toTest3);
        assertNotEqualsHashCode(toTest5, toTest4);

        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(new Object()));
        assertFalse(toTest1.equals("first second"));
        assertFalse(toTest1.equals(new Pair<Object, Object>(new Object(), new Object())));
    }
}
