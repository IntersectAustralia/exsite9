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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.google.common.base.Objects;


/**
 * Represents a research data file in a folder that the researcher has associated with a project.
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"PROJECT","FILE"}))
@Converter(name="fileToStringConverter",
    converterClass=au.org.intersect.exsite9.domain.utils.FileToStringConverter.class)
public final class ResearchFile
{
    @Id
    @GeneratedValue
    private Long id;

    @Convert("fileToStringConverter")
    private File file;

    @ManyToOne
    private Project project;

    public ResearchFile()
    {
    }

    public ResearchFile(final File fileOnDisk)
    {
        this.file = fileOnDisk;
    }

    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(final Project project)
    {
        this.project = project;
    }

    public File getFile()
    {
        return this.file;
    }

    public void setFile(final File file)
    {
        this.file = file;
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
        return Objects.equal(this.file, other.file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("file", this.file);
        return tsb.toString();
    }
}
