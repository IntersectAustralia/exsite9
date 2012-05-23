/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a base node in group/file/project Hierarchy.
 */
public abstract class Node
{

    /**
     * The name given to the node.
     */
    private final String name;

    /**
     * The child nodes that are encapsulated as part of this node.
     */
    private final Set<Group> groups = new HashSet<Group>();

    /**
     * The child files that are encapsulated as part of this node.
     */
    private final Set<File> files = new HashSet<File>();

    /**
     * Constructor
     * 
     * @param name
     *            The name of the node.
     */
    public Node(final String name)
    {
        this.name = name;
    }

    /**
     * Obtains the name of the node.
     * 
     * @return The name of the node.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Obtains child groups.
     * 
     * @return Child groups.
     */
    public Set<Group> getGroups()
    {
        return this.groups;
    }

    /**
     * Obtains child files that this node contains.
     * 
     * @return Child files.
     */
    public Set<File> getFiles()
    {
        return this.files;
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
        if (!(obj instanceof Node))
        {
            return false;
        }
        final Node other = (Node) obj;
        return Objects.equal(this.name, other.name) && Objects.equal(this.groups, other.groups)
                && Objects.equal(this.files, other.files);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.name, this.groups, this.files);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("name", this.name);
        tsb.append("groups", this.groups);
        tsb.append("files", this.files);
        return tsb.toString();
    }
}
