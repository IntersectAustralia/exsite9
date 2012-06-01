package au.org.intersect.exsite9.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.eclipse.persistence.exceptions.DatabaseException;

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
    					Throwable cause = pe.getCause();
    					if (cause instanceof DatabaseException)
    					{
    						cause = cause.getCause();
    						if (cause instanceof SQLIntegrityConstraintViolationException)
    						{
    							// TODO: log this
    							// continue -- tried to insert a duplicate file
    						}
    						else
    						{
    							throw pe;
    						}
    					}
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
