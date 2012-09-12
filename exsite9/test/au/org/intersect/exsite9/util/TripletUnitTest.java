/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static au.org.intersect.exsite9.test.Assert.assertNotEqualsHashCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests {@link Triplet}
 */
public final class TripletUnitTest
{
    @Test
    public void testTriplet()
    {
        final String first = "first";
        final String second = "second";
        final String third = "third";

        final Triplet<String, String, String> toTest1 = new Triplet<String, String, String>(first, second, third);

        assertEquals(first, toTest1.getFirst());
        assertEquals(second, toTest1.getSecond());
        assertEquals(third, toTest1.getThird());

        assertTrue(toTest1.toString().contains("first=first"));
        assertTrue(toTest1.toString().contains("second=second"));
        assertTrue(toTest1.toString().contains("third=third"));
    }

    @Test
    public void testEqualsHashCode()
    {
        final String first = "first";
        final String second = "second";
        final String third = "third";
        final Triplet<String, String, String> toTest1 = new Triplet<String, String, String>(first, second, third);
        final Triplet<String, String, String> toTest2 = new Triplet<String, String, String>(first, second, third);
        final Triplet<String, String, String> toTest3 = new Triplet<String, String, String>(first, second, "xyz");

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertTrue(toTest1.hashCode() == toTest2.hashCode());

        assertNotEqualsHashCode(toTest2, toTest3);
        assertNotEqualsHashCode(toTest3, toTest2);

        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(new Object()));
        assertFalse(toTest1.equals("first second"));
        assertFalse(toTest1.equals(new Triplet<Object, Object, Object>(new Object(), new Object(), new Object())));
    }
}
