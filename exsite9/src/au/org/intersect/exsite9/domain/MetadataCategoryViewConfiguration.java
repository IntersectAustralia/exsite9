/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Objects;

/**
 * Holds view configuration details of a {@link MetadataCategory}
 */
@Entity
@Table(name="METADATA_CATEGORY_VIEW_CONFIGURATION")
public final class MetadataCategoryViewConfiguration implements Serializable
{
    private static final long serialVersionUID = -701071381210288931L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private MetadataCategory metadataCategory;

    private Boolean expanded;

    public MetadataCategoryViewConfiguration(final MetadataCategory metadataCategory, final Boolean expanded)
    {
        this.metadataCategory = metadataCategory;
        this.expanded = expanded;
    }

    public MetadataCategoryViewConfiguration()
    {
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

    public Boolean getExpanded()
    {
        return expanded;
    }

    public void setExpanded(final Boolean expanded)
    {
        this.expanded = expanded;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof MetadataCategoryViewConfiguration))
        {
            return false;
        }
        final MetadataCategoryViewConfiguration other = (MetadataCategoryViewConfiguration) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }
}
