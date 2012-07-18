/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.google.common.base.Objects;

/**
 * Represents a folder on the researcher's desktop that contains research data files
 */
@Entity
@Converter(name="fileToStringConverter",
converterClass=au.org.intersect.exsite9.domain.utils.FileToStringConverter.class)
public final class Folder implements Serializable
{
    private static final long serialVersionUID = 8420843643147102477L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Convert("fileToStringConverter")
    private File folder;
    
    private long lastCheckTimeInMillis = 0L;
    
    @OneToMany
    private List<ResearchFile> files = new ArrayList<ResearchFile>(0);
    
    public Folder()
    {
    }

    public Folder(File folder)
    {
        this.folder = folder;
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

    public File getFolder()
    {
        return this.folder;
    }
    
    public void setFolder(File folder)
    {
        this.folder = folder;
        
    }

    public List<ResearchFile> getFiles()
    {
        return files;
    }

    public void setFiles(List<ResearchFile> files)
    {
        this.files = files;
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
        return Objects.equal(this.folder.getName(), other.folder.getName()) && Objects.equal(this.folder.getAbsolutePath(), other.folder.getAbsolutePath());
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.folder.getName()).append(this.folder.getAbsolutePath()).toHashCode();
    }

}
