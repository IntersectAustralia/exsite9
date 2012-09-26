/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * the value entered for a metadata attribute for a {@link ResearchFile} or {@link Group}
 */
@Entity
@Table(name="METADATA_ATTRIBUTE_VALUE")
public final class MetadataAttributeValue implements Serializable
{
    private static final long serialVersionUID = -1856850221041658159L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "longvarchar")
    private String value;

    public MetadataAttributeValue()
    {
    }

    public MetadataAttributeValue(final String value)
    {
        this.value = value;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
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
        if (!(obj instanceof MetadataAttributeValue))
        {
            return false;
        }
        final MetadataAttributeValue other = (MetadataAttributeValue) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("value", this.value).toString();
    }
}
