/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Predicate;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;

/**
 * Tests {@link NewFilesGroupPredicate}
 */
public final class NewFilesGroupPredicateUnitTest
{
    @Test
    public void testNewFilesGroupPredicate()
    {
        final NewFilesGroupPredicate toTest = NewFilesGroupPredicate.INSANCE;
        assertTrue(toTest instanceof Predicate<?>);

        assertTrue(toTest.apply(new NewFilesGroup()));
        assertFalse(toTest.apply(new Group("some other group")));
    }
}
