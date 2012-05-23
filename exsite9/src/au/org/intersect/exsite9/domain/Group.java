/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a Group.
 */
public final class Group extends Node
{

    /**
     * Constructor
     * 
     * @param name
     *            The name of the group.
     */
    public Group(final String name)
    {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Group))
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.appendSuper(super.toString());
        return tsb.toString();
    }
}
