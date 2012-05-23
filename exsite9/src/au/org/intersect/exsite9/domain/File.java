/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a File.
 */
public final class File
{

    /**
     * The name of the file.
     */
    private final String name;

    /**
     * Constructor
     * 
     * @param name
     *            The name of the file.
     */
    public File(final String name)
    {
        this.name = name;
    }

    /**
     * Obtains the name of the file.
     * 
     * @return the name of the file.
     */
    public String getName()
    {
        return this.name;
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
        if (!(obj instanceof File))
        {
            return false;
        }
        final File other = (File) obj;
        return Objects.equal(this.name, other.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("name", this.name);
        return tsb.toString();
    }
}
