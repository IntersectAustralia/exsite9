package au.org.intersect.exsite9.service;

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
	 // TODO: Run this in its own thread.
    
	    EntityManager em = entityManagerFactory.getEntityManager();
	    try
	    {
    	    ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
    	    ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
    	    
    		for(Folder folder : project.getFolders())
    		{
    			List<ResearchFile> newFileList = FolderHelper.getAllFilesInFolder(folder);
    			for(ResearchFile researchFile : newFileList)
    			{
    			    // If there is already a research file in the database, do not insert another.
    			    final ResearchFile existing = researchFileDAO.findByPath(project, researchFile.getFile());
    			    if (existing == null)
    			    {
    			        researchFile.setProjectID(project.getId());
    			        researchFileDAO.createResearchFile(researchFile);
    			        project.getNewFilesNode().getResearchFiles().add(researchFile);
    			    }
    			}
    			projectDAO.updateProject(project);
    		}
	    }
	    finally
	    {
	        em.close();
	    }
    }
}
