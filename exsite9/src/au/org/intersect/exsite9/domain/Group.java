/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a Group.
 */
public final class Group extends Node implements Serializable
{

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1700329934000740424L;
	
	@Id
    @GeneratedValue
    private Long id;
	
	public Group()
	{
		super();
	}
	
    /**
     * Constructor
     * 
     * @param name
     *            The name of the group.
     */
    public Group(final String name)
    {
        super(name);
    }

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Group))
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.appendSuper(super.toString());
        return tsb.toString();
    }
}
