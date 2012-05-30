package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.helper.FolderHelper;

public class FileService implements IFileService
{
	private final ResearchFileDAO researchFileDAO;
	private final ProjectDAO projectDAO;
	
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
				researchFileDAO.createResearchFile(researchFile);
				project.getNewFilesNode().getResearchFiles().add(researchFile);
			}
			projectDAO.updateProject(project);
		}
    }
}
