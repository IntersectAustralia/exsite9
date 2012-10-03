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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * Represents a Group.
 */
@Entity
@Table(name="RESEARCH_GROUP")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Group implements Serializable, IMetadataAssignable
{
	private static final long serialVersionUID = 1700329934000740424L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	@Column(length = 255)
	private String name;

	@OneToMany
	private final List<Group> groups = new ArrayList<Group>();

	@ManyToOne
	private Group parentGroup;

	@OneToMany
	private final List<ResearchFile> researchFiles = new ArrayList<ResearchFile>();

    @OneToMany
    private final List<MetadataAssociation> metadataAssociations = new ArrayList<MetadataAssociation>();
    
    @ManyToOne
    private Project project;

    /**
     * The field to sort child research files on.
     */
    private ResearchFileSortField researchFileSortField;

    /**
     * The direction to sort child research files on.
     */
    private SortFieldDirection researchFileSortDirection;
	
	public Group()
	{
		name = "";
		researchFileSortField = ResearchFileSortField.NAME;
		researchFileSortDirection = SortFieldDirection.ASC;
	}
	
    public Group(final String name)
    {
        this.name = name;
        researchFileSortField = ResearchFileSortField.NAME;
        researchFileSortDirection = SortFieldDirection.ASC;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

    public List<Group> getGroups()
    {
        return groups;
    }

    public List<ResearchFile> getResearchFiles()
    {
        return researchFiles;
    }

    public List<MetadataAssociation> getMetadataAssociations()
    {
        return this.metadataAssociations;
    }

    public Group getParentGroup()
    {
        return this.parentGroup;
    }

    public void setParentGroup(final Group parentGroup)
    {
        this.parentGroup = parentGroup;
    }

    public ResearchFileSortField getResearchFileSortField()
    {
        return this.researchFileSortField;
    }

    public void setResearchFileSortField(final ResearchFileSortField sortField)
    {
        this.researchFileSortField = sortField;
    }

    public SortFieldDirection getResearchFileSortDirection()
    {
        return this.researchFileSortDirection;
    }

    public void setResearchFileSortDirection(final SortFieldDirection sortDirection)
    {
        this.researchFileSortDirection = sortDirection;
    }
    
    public Project getProject()
    {
        return project;
    }

    public void setProject(final Project project)
    {
        this.project = project;
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
        final Group other = (Group) obj;
        return Objects.equal(this.id, other.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id);
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
        return tsb.toString();
    }
    
    /**
     * Returns true if this group is an ancestor of the specified group
     * @param group The specified group
     * @return true if this group is an ancestor of the specified group, false otherwise
     */
    public boolean isAnAncestorOf(Group group)
    {
        if (groups.contains(group))
        {
            return true;
        }
        
        for(Group childGroup : this.groups)
        {
            if(childGroup.isAnAncestorOf(group))
            {
                return true;
            }
        }
        
        return false;
    }
}
