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

import javax.persistence.CascadeType;
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
 * A metadata category.
 */
@Entity
@Table(name="METADATA_CATEGORY")
public final class MetadataCategory implements Serializable
{
    private static final long serialVersionUID = -6662974846879710713L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;
    
    @Column(columnDefinition = "longvarchar")
    private String description;
    
    private MetadataCategoryUse use;

    private MetadataCategoryType type;
    
    private boolean isInextensible;


    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    private List<MetadataValue> metadataValues = new ArrayList<MetadataValue>();

    public MetadataCategory()
    {
    }

    public MetadataCategory(final String name, final MetadataCategoryType type, final MetadataCategoryUse use)
    {
        this.name = name;
        this.use = use;
        this.type = type;
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
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<MetadataValue> getValues()
    {
        return this.metadataValues;
    }
    
    public void setValues(List<MetadataValue> values)
    {
        this.metadataValues = values;
    }

    public MetadataCategoryUse getUse()
    {
        return use;
    }

    public void setUse(MetadataCategoryUse use)
    {
        this.use = use;
    }

    public MetadataCategoryType getType()
    {
        return this.type;
    }

    public void setType(final MetadataCategoryType metadataCategoryType)
    {
        this.type = metadataCategoryType;
    }

    public boolean isInextensible()
    {
        return isInextensible;
    }
    
    public void setInextensible(boolean isInextensible)
    {
        this.isInextensible = isInextensible;
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
        if (!(obj instanceof MetadataCategory))
        {
            return false;
        }
        final MetadataCategory other = (MetadataCategory) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("type", this.type).append("use",this.use).toString();
    }
}