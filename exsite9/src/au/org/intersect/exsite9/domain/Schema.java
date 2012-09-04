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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a Schema
 */
@Entity
public final class Schema implements Serializable
{
    private static final long serialVersionUID = 2788090548886349473L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "longvarchar")
    private String description;

    @Column(columnDefinition = "longvarchar")
    private String namespaceURL;

    private Boolean local;

    @OneToMany
    private List<MetadataCategory> metadataCategories;

    public Schema()
    {
        this.name = "";
        this.description = "";
        this.namespaceURL = "";
        this.metadataCategories = new ArrayList<MetadataCategory>();
    }

    public Schema(final String name, final String description, final String namespaceURL, final Boolean local)
    {
        this.name = name;
        this.description = description;
        this.namespaceURL = namespaceURL;
        this.local = local;
        this.metadataCategories = new ArrayList<MetadataCategory>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getNamespaceURL()
    {
        return namespaceURL;
    }

    public void setNamespaceURL(final String namespaceURL)
    {
        this.namespaceURL = namespaceURL;
    }

    public Boolean getLocal()
    {
        return this.local;
    }

    public void setLocal(final Boolean local)
    {
        this.local = local;
    }

    public List<MetadataCategory> getMetadataCategories()
    {
        return this.metadataCategories;
    }

    public void setMetadataCategories(final List<MetadataCategory> mdcs)
    {
        this.metadataCategories = mdcs;
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
        if (!(obj instanceof Schema))
        {
            return false;
        }
        final Schema other = (Schema) obj;
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
    
    public List<MetadataCategory> getRequiredMetadataCategories()
    {
        final List<MetadataCategory> requiredCategories = new ArrayList<MetadataCategory>();
        for (final MetadataCategory category : this.metadataCategories)
        {
            if (category.getUse() == MetadataCategoryUse.required)
            {
                requiredCategories.add(category);
            }
        }
        
        return requiredCategories;
    }
}
