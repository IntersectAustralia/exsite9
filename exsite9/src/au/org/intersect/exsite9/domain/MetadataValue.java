package au.org.intersect.exsite9.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

@Entity
@Table(name="METADATA_VALUE")
public class MetadataValue implements Serializable
{
    private static final long serialVersionUID = 6279526422015017195L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String value;

    public MetadataValue()
    {
    }

    public MetadataValue(final String name)
    {
        this.value = name;
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
        return new HashCodeBuilder().append(this.value).toHashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof MetadataValue))
        {
            return false;
        }
        final MetadataValue other = (MetadataValue) obj;
        return Objects.equal(this.value, other.value) && Objects.equal(this.id, other.id);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", this.id).append("value", this.value).toString();
    }
}
