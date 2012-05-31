/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.property;

import org.eclipse.core.expressions.PropertyTester;

import au.org.intersect.exsite9.domain.Group;

/**
 * A Property Tester used so we can include names of groups when determining when to add "New Group" menu items to tree nodes.
 */
public class GroupNameTester extends PropertyTester
{

    public GroupNameTester()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue)
    {
        final Group group = (Group) receiver;
        return group.getName().equals(expectedValue);
    }
}
