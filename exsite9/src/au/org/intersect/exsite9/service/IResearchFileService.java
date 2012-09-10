/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * A Service to provide actions that may be performed with Research Files.
 */
public interface IResearchFileService 
{
    /**
     * Identifies new files for a project.
     * @param project The project to identify new files for.
     */
	public void identifyNewFilesForProject(Project project);
	
	/**
	 * Identifies new files for a project in a folder.
	 * @param project The project to identify new files for.
	 * @param folder The folder to identify new files in.
	 */
	public void identifyNewFilesForFolder(Project project, Folder folder);
	
	/**
	 * Associates metadata to a research file.
	 * @param file The file.
	 * @param metadataCategory The metadata category to associate.
	 * @param metadataValue The metadata value to associate.
	 */
	public void associateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue, final MetadataAttributeValue metadataAttributeValue);

	/**
	 * Disassociates metadata from a research file. 
	 * @param file The file.
	 * @param metadataCategory The metadata category to disassociate.
	 * @param metadataValue The metadata value to disassociate.
	 */
	public void disassociateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue);

	/**
	 * Dissassociates multiple metadata values from a research file.
	 * @param file The file.
     * @param metadataCategory The metadata category to disassociate.
     * @param metadataValue The metadata values to disassociate.
	 */
	public void disassociateMultipleMetadataValues(final ResearchFile file, final MetadataCategory metadataCategory, final List<MetadataValue> metadataValues);

	/**
	 * Obtains research files with a metadata association.
	 * @param metadataCategory The metadata category of the association.
	 * @param metadataValue The metadata value of the assocation.
	 * @return The research files with the provided assocation.
	 */
	public List<ResearchFile> getResearchFilesWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue);

	/**
	 * Obtains a research file by it's ID.
	 * @param id The ID of the research file to find.
	 * @return The research file.
	 */
	public ResearchFile findResearchFileByID(final Long id);

	/**
	 * Updates a research file.
	 * @param selectionObject The research file to update.
	 */
    public void updateResearchFile(ResearchFile selectionObject);

    /**
     * Consolidates a sub folder into its parent folder.
     * @param project The project to perform the consolidation for.
     * @param parentFolder The parent folder.
     * @param subFolder The sub folder.
     */
    public void consolidateSubFolderIntoParentFolder(final Project project, final Folder parentFolder, final Folder subFolder);
    
    /**
     * Import an existing folder structure into the project creating new Groups for folders and new ResearchFiles for files.
     * The created group will be a child of the Project.
     * @param project
     * @param folder
     */
    public void importFolderStructureForProject(final Project project, Folder folder);

    public void changeAFilesParentFolder(ResearchFile researchFile, long newFolderId);
    
}
