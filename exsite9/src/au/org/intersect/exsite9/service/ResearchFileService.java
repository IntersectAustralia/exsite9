package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.helper.FolderHelper;

public class ResearchFileService implements IResearchFileService
{
    private final EntityManagerFactory entityManagerFactory;
	private final ResearchFileDAOFactory researchFileDAOFactory;
	private final ProjectDAOFactory projectDAOFactory;
	
	public ResearchFileService(EntityManagerFactory entityManagerFactory,
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
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
            
            List<File> newFileList = FolderHelper.getAllFilesInFolder(folder);
            for (final File file : newFileList)
            {
                // If there is already a research file in the database, do not insert another.
                final ResearchFile researchFile = new ResearchFile(file);
                
                if(! folder.getFiles().contains(researchFile))
                {
                    researchFile.setProject(project);
                    researchFileDAO.createResearchFile(researchFile);
                    folder.getFiles().add(researchFile);
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
