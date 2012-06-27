/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

/**
 * 
 */
public interface IResearchFileService 
{
	public void identifyNewFilesForProject(Project project);
	
	public void identifyNewFilesForFolder(Project project, Folder folder);
}
