/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a Research Project
 */
@Entity
public final class Project
{

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    
    @ManyToMany
    @JoinTable(
            name="project_folder",
            joinColumns={@JoinColumn(name="project_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="folder_id", referencedColumnName="id")})
    private List<Folder> folders;
    
    // TODO: Persist the root node
    @Transient
    private Group rootNode;
    
    public Project()
    {
        name = "";
        description = "";
    }

    public Project(final String name, final String description)
    {
        this.name = name;
        this.description = description;
        this.rootNode = new Group(this.name);
    }

    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
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

    
    public Group getRootNode()
    {
        return rootNode;
    }

    public void setRootNode(Group rootNode)
    {
        this.rootNode = rootNode;
    }

    
    public List<Folder> getFolders()
    {
        return folders;
    }

    public void setFolders(List<Folder> folders)
    {
        this.folders = folders;
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
        if (!(obj instanceof Project))
        {
            return false;
        }
        Project other = (Project) obj;
        return Objects.equal(this.name, other.name) && Objects.equal(this.description, other.description) 
                && Objects.equal(this.rootNode, other.rootNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.name, this.description, this.rootNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append(this.rootNode);
        tsb.appendSuper(super.toString());
        return tsb.toString();
    }
}
