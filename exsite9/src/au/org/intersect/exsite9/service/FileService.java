package au.org.intersect.exsite9.service;

import java.util.List;

import javax.persistence.PersistenceException;

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.helper.FolderHelper;

public class FileService implements IFileService
{
	private ResearchFileDAO researchFileDAO = null;
	private ProjectDAO projectDAO = null;
	
	public FileService(ProjectDAO projectDAO, ResearchFileDAO researchFileDAO)
	{
		this.researchFileDAO = researchFileDAO;
		this.projectDAO = projectDAO;
	}
	
	@Override
    public void identifyNewFilesForProject(Project project)
    {
		// TODO: Run this in its own thread.
		for(Folder folder : project.getFolders())
		{
			List<ResearchFile> newFileList = FolderHelper.identifyNewFiles(folder);
			for(ResearchFile researchFile : newFileList)
			{
				try
				{
					researchFileDAO.createResearchFile(researchFile);
					project.getNewFilesNode().getResearchFiles().add(researchFile);
				}
				catch(PersistenceException pe)
				{
					// continue without adding file to list of new files
				}
			}
			projectDAO.updateProject(project);
		}
    }
}
