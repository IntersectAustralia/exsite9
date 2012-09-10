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
 * A metadata attribute.
 */
@Entity
@Table(name="METADATA_ATTRIBUTE")
public final class MetadataAttribute implements Serializable
{
    private static final long serialVersionUID = 8733761857585926013L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    private List<MetadataAttributeValue> metadataAttributeValues = new ArrayList<MetadataAttributeValue>();

    public MetadataAttribute()
    {
    }

    public MetadataAttribute(final String name, final List<MetadataAttributeValue> values)
    {
        this.name = name;
        this.metadataAttributeValues.addAll(values);
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

    public List<MetadataAttributeValue> getMetadataAttributeValues()
    {
        return metadataAttributeValues;
    }

    public void setMetadataAttributeValues(final List<MetadataAttributeValue> metadataAttributeValues)
    {
        this.metadataAttributeValues = metadataAttributeValues;
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
        if (!(obj instanceof MetadataAttribute))
        {
            return false;
        }
        final MetadataAttribute other = (MetadataAttribute) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).toString();
    }
}
