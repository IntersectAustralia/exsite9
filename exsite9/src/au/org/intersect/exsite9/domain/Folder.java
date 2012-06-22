/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Objects;

/**
 * Represents a folder on the researcher's desktop that contains research data files
 */
@Entity
public final class Folder implements Serializable
{
    private static final long serialVersionUID = 8420843643147102477L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    private long lastCheckTimeInMillis = 0L;
    
    public Folder()
    {
        name = "";
        path = "";
    }
    
    public Folder(String name, String path)
    {
        this.name = name;
        this.path = path;
    }

    public Folder(File fileOnDisk)
    {
        this.name = fileOnDisk.getName();
        this.path = fileOnDisk.getAbsolutePath();
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public long getLastCheckTimeInMillis()
    {
        return lastCheckTimeInMillis;
    }

    public void setLastCheckTimeInMillis(long lastCheckTimeInMillis)
    {
        this.lastCheckTimeInMillis = lastCheckTimeInMillis;
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Folder))
        {
            return false;
        }
        final Folder other = (Folder) obj;
        return Objects.equal(this.name, other.name) && Objects.equal(this.path, other.path);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.name).append(this.path).toHashCode();
    }
}
