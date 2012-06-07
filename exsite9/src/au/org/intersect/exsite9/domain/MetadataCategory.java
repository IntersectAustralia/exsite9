/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A metadata category.
 */
@Entity
@Table(name="METADATA_CATEGORY")
public final class MetadataCategory implements Serializable
{
    private static final long serialVersionUID = -6662974846879710713L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Set<String> metadataValues = new HashSet<String>();

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

    public Set<String> getValues()
    {
        return this.metadataValues;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.name).toHashCode();
    }
}
