/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * 
 */
public interface IResearchFileService 
{
	public void identifyNewFilesForProject(Project project);
	
	public void identifyNewFilesForFolder(Project project, Folder folder);
	
	public void associateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue);
	
	public void disassociateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue);
	
	public List<ResearchFile> getResearchFilesWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue);

	public ResearchFile findResearchFileByID(final Long id);
}
