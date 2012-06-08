/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
    private String owner;
    private String description;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="project_folder",
            joinColumns={@JoinColumn(name="project_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="folder_id", referencedColumnName="id")})
    private List<Folder> folders;

    private List<MetadataCategory> metadataCategories;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Group rootNode;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Group newFilesNode;
    
    public Project()
    {
        name = "";
        owner = "";
        description = "";
    }

    public Project(final String name, final String owner, final String description)
    {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.folders = new ArrayList<Folder>(0);
        this.rootNode = new Group(this.name);
        this.newFilesNode = new NewFilesGroup();
        this.rootNode.getGroups().add(newFilesNode);
        this.metadataCategories = new ArrayList<MetadataCategory>();
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

    
    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public Group getRootNode()
    {
        return rootNode;
    }

    public void setRootNode(Group rootNode)
    {
        this.rootNode = rootNode;
    }

    
    public Group getNewFilesNode() {
		return newFilesNode;
	}

	public void setNewFilesNode(Group newFilesNode) {
		this.newFilesNode = newFilesNode;
	}

	public List<Folder> getFolders()
    {
        return folders;
    }

    public void setFolders(List<Folder> folders)
    {
        this.folders = folders;
    }

    public List<MetadataCategory> getMetadataCategories()
    {
        return this.metadataCategories;
    }

    public void setMetadataCategories(final List<MetadataCategory> mdcs)
    {
        this.metadataCategories = mdcs;
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
        return Objects.equal(this.name, other.name) 
            && Objects.equal(this.description, other.description) 
            && Objects.equal(this.owner, other.owner)
            && Objects.equal(this.rootNode, other.rootNode)
            && Objects.equal(this.metadataCategories, other.metadataCategories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.name, this.description, this.owner, this.rootNode, this.metadataCategories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("id", this.id);
        tsb.append("name", this.name);
        tsb.append("description", this.description);
        tsb.append("owner", this.owner);
        return tsb.toString();
    }
}
