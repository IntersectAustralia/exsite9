package au.org.intersect.exsite9.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Objects;

/**
 * Australian and New Zealand Standard Research Classification (ANZSRC), 2008.
 * {@see http://www.abs.gov.au/AUSSTATS/abs@.nsf/Latestproducts/17DC1A688895C0C1CA257418000538FC?opendocument}
 */
@Entity
public final class FieldOfResearch implements Serializable
{
    private static final long serialVersionUID = -7901972332154106702L;

    @Id
    private String code;

    private String name;

    public FieldOfResearch()
    {
    }

    public FieldOfResearch(final String code, final String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(final String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @{inheritDoc}
     */
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof FieldOfResearch))
        {
            return false;
        }
        final FieldOfResearch other = (FieldOfResearch) obj;
        return Objects.equal(this.code, other.code);
    }

    /**
     * @{inheritDoc}
     */
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.code).toHashCode();
    }

    @Override
    public String toString()
    {
        return this.code + " - " + this.name;
    }
}
