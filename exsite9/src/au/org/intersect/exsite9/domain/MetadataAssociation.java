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
 * An association between a metadata category and it's values that have been assigned.
 * Used when adding metadata to {@link ResearchFile}s.
 */
@Entity
@Table(name="METADATA_ASSOCIATION")
public final class MetadataAssociation implements Serializable
{
    private static final long serialVersionUID = 2152083468103054575L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private MetadataCategory metadataCategory;

    @OneToMany
    private final List<MetadataValue> metadataValues = new ArrayList<MetadataValue>();

    public MetadataAssociation()
    {
    }

    public MetadataAssociation(final MetadataCategory metadataCategory)
    {
        this.metadataCategory = metadataCategory;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public MetadataCategory getMetadataCategory()
    {
        return metadataCategory;
    }

    public void setMetadataCategory(final MetadataCategory metadataCategory)
    {
        this.metadataCategory = metadataCategory;
    }

    public List<MetadataValue> getMetadataValues()
    {
        return metadataValues;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof MetadataAssociation))
        {
            return false;
        }
        final MetadataAssociation other = (MetadataAssociation) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.metadataCategory, other.metadataCategory) &&
                Objects.equal(this.metadataValues, other.metadataValues);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("metadataCategory", this.metadataCategory).toString();
    }
}