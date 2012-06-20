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
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SEQ_METADATA_CATEGORY")
    private Long id;

    private String name;

    @OneToMany(cascade=CascadeType.ALL)
    private List<MetadataValue> metadataValues = new ArrayList<MetadataValue>();

    public MetadataCategory()
    {
    }

    public MetadataCategory(final String name)
    {
        this.name = name;
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

    public List<MetadataValue> getValues()
    {
        return this.metadataValues;
    }
    
    public void setValues(List<MetadataValue> values)
    {
        this.metadataValues = values;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.name).toHashCode();
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
        return Objects.equal(this.id, other.id) && Objects.equal(this.name, other.name);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).toString();
    }
}
