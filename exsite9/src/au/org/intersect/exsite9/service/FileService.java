package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.helper.FolderHelper;

public class FileService implements IFileService
{
    private final ExSite9EntityManagerFactory entityManagerFactory;
	private final ResearchFileDAOFactory researchFileDAOFactory;
	private final ProjectDAOFactory projectDAOFactory;
	
	public FileService(ExSite9EntityManagerFactory entityManagerFactory,
	                   ProjectDAOFactory projectDAOFactory,
	                   ResearchFileDAOFactory researchFileDAOFactory)
	{
	    this.entityManagerFactory = entityManagerFactory;
		this.researchFileDAOFactory = researchFileDAOFactory;
		this.projectDAOFactory = projectDAOFactory;
	}
	
	@Override
    public void identifyNewFilesForProject(Project project)
    {
	    for(Folder folder : project.getFolders())
		{
	        addFilesFromFolder(project, folder);
		}
    }

    @Override
    public void identifyNewFilesForFolder(Project project, Folder folder)
    {
        addFilesFromFolder(project, folder);
    }

    private void addFilesFromFolder(Project project, Folder folder)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
            
            List<File> newFileList = FolderHelper.getAllFilesInFolder(folder);
            for (final File file : newFileList)
            {
                // If there is already a research file in the database, do not insert another.
                final ResearchFile existing = researchFileDAO.findByPath(project, file);
                if (existing == null)
                {
                    final ResearchFile researchFile = new ResearchFile(file);
                    researchFile.setProject(project);
                    researchFileDAO.createResearchFile(researchFile);
                    project.getNewFilesNode().getResearchFiles().add(researchFile);
                }
            }
            projectDAO.updateProject(project);
        }
        finally
        {
            em.close();
        }
    }

}
