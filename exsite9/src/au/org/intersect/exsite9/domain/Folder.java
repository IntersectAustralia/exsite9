/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Represents a folder on the researcher's desktop that contains research data files
 */
@Entity
public final class Folder
{
    @Id
    @GeneratedValue
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
}
