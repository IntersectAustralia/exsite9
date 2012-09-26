/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.property;

import org.eclipse.core.expressions.PropertyTester;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests if a research file already exists in the system
 */
public class FileExistsTester extends PropertyTester
{
    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
        return ((ResearchFile) receiver).getFile().exists();
    }

}
