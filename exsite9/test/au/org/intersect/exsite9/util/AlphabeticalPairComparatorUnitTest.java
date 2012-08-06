/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link AlphabeticalPairComparator}
 */
public final class AlphabeticalPairComparatorUnitTest
{
    @Test
    public void testAlphabeticalPairComparator()
    {
        final AlphabeticalPairComparator toTest = new AlphabeticalPairComparator();
        assertTrue(toTest.compare(new Pair<String, String>("a", "b"), new Pair<String, String>("a", "b")) == 0);
        assertTrue(toTest.compare(new Pair<String, String>("A", "B"), new Pair<String, String>("a", "b")) == 0);
        assertTrue(toTest.compare(new Pair<String, String>("a", "b"), new Pair<String, String>("A", "B")) == 0);

        assertTrue(toTest.compare(new Pair<String, String>("a", "b"), new Pair<String, String>("b", "c")) < 0);
        assertTrue(toTest.compare(new Pair<String, String>("b", "b"), new Pair<String, String>("a", "c")) > 0);

        assertTrue(toTest.compare(new Pair<String, String>("a", "a"), new Pair<String, String>("a", "b")) < 0);
        assertTrue(toTest.compare(new Pair<String, String>("a", "b"), new Pair<String, String>("a", "a")) > 0);
    }
}
