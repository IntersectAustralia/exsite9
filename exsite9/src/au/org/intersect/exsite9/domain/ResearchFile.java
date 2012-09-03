/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import com.google.common.base.Objects;

/**
 * Represents a research data file in a folder that the researcher has associated with a project.
 */
@Entity
@Table(name="RESEARCH_FILE", uniqueConstraints=@UniqueConstraint(columnNames={"PROJECT_ID","FILE"}))
@Converter(name="fileToStringConverter",
    converterClass=au.org.intersect.exsite9.domain.utils.FileToStringConverter.class)
public final class ResearchFile implements Serializable, IMetadataAssignable
{

    private static final long serialVersionUID = 5047390909436811919L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Convert("fileToStringConverter")
    private File file;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Group parentGroup;
    
    @OneToMany
    private final List<MetadataAssociation> metadataAssociations = new ArrayList<MetadataAssociation>();

    public ResearchFile()
    {
    }

    public ResearchFile(final File fileOnDisk)
    {
        this.file = fileOnDisk;
    }

    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(final Project project)
    {
        this.project = project;
    }

    public File getFile()
    {
        return this.file;
    }

    public void setFile(final File file)
    {
        this.file = file;
    }

    public void setParentGroup(final Group parentGroup)
    {
        this.parentGroup = parentGroup;
    }

    public Group getParentGroup()
    {
        return this.parentGroup;
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
        if (!(obj instanceof ResearchFile))
        {
            return false;
        }
        final ResearchFile other = (ResearchFile) obj;
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
        return new ToStringBuilder(this).append("id", this.id).append("file", this.file).toString();
    }
    
    public List<MetadataAssociation> getMetadataAssociations()
    {
        return this.metadataAssociations;
    }
    
    public boolean isMissingRequiredMetadata()
    {
        boolean missing = false;
        
        category: 
        for(final MetadataCategory category : getProject().getSchema().getRequiredMetadataCategories())
        {
            for(final MetadataAssociation association : getMetadataAssociations())
            {
                if(association.getMetadataCategory().equals(category))
                {
                    continue category;
                }
            }
            missing = true;
            break;
        }
        
        return missing;
    }
    
    public String getDisplayDate()
    {
        String displayDate = "";
        if (this.file != null)
        {
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm aa");
                displayDate = sdf.format(new Date(this.file.lastModified()));
            }
            catch(Exception e){
                // continue
            }
        }
        return displayDate;
    }
    
    public String getDisplaySize()
    {
        String displaySize = "0 B";
        if(this.file != null){
            try{
                long size = this.file.length();
                final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
                int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
                displaySize =  new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
            }
            catch(Exception e){
                // continue
            }
        }
        return displaySize;
    }

}
