/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a research data file in a folder that the researcher has associated with a project.
 */
public final class ResearchFile
{

    private final String name;
    private final String path;

    public ResearchFile(final String name)
    {
        this.name = name;
        this.path = "";
    }
    
    public ResearchFile(File fileOnDisk)
    {
        this.name = fileOnDisk.getName();
        this.path = fileOnDisk.getAbsolutePath();
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
        if (!(obj instanceof ResearchFile))
        {
            return false;
        }
        final ResearchFile other = (ResearchFile) obj;
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
        tsb.append("path", this.path);
        return tsb.toString();
    }
}
