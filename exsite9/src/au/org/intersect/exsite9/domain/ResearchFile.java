/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a research data file in a folder that the researcher has associated with a project.
 */
@Entity
public final class ResearchFile
{
    @Id
    @GeneratedValue
    private Long id;
    private final String name;
    
    @Column(unique=true)
    private final String path;

    public ResearchFile()
    {
        name = "";
        path = "";
    }
    
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

    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
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
