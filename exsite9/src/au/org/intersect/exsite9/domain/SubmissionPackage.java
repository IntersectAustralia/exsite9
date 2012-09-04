/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * A Submission Package is a collection of {@link ResearchFile}s that are to be bundled up for submission.
 */
@Entity
@Table(name="SUBMISSION_PACKAGE")
public final class SubmissionPackage implements Serializable
{
    private static final long serialVersionUID = -7263488588094251324L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "longvarchar")
    private String description;

    @OneToMany
    private final List<ResearchFile> researchFiles = new ArrayList<ResearchFile>();

    public SubmissionPackage()
    {
    }

    public SubmissionPackage(final String name, final String description, final List<ResearchFile> researchFiles)
    {
        this.name = name;
        this.description = description;
        this.researchFiles.addAll(researchFiles);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public List<ResearchFile> getResearchFiles()
    {
        return this.researchFiles;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof SubmissionPackage))
        {
            return false;
        }
        final SubmissionPackage other = (SubmissionPackage) obj;
        return Objects.equal(this.id, other.id);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).toString();
    }
}
