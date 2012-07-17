package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.MetadataAssociationDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.util.FolderUtils;

public class ResearchFileService implements IResearchFileService
{
    private static final Logger LOG = Logger.getLogger(ResearchFileService.class);
    private final EntityManagerFactory entityManagerFactory;
	private final ResearchFileDAOFactory researchFileDAOFactory;
	private final ProjectDAOFactory projectDAOFactory;
	private final MetadataAssociationDAOFactory metadataAssociationDAOFactory;
	private final FolderDAOFactory folderDAOFactory;
	
	public ResearchFileService(final EntityManagerFactory entityManagerFactory,
	                   final ProjectDAOFactory projectDAOFactory,
	                   final ResearchFileDAOFactory researchFileDAOFactory,
	                   final MetadataAssociationDAOFactory metadataAssociationDAOFactory,
	                   final FolderDAOFactory folderDAOFactory)
	{
	    this.entityManagerFactory = entityManagerFactory;
		this.researchFileDAOFactory = researchFileDAOFactory;
		this.projectDAOFactory = projectDAOFactory;
		this.metadataAssociationDAOFactory = metadataAssociationDAOFactory;
		this.folderDAOFactory = folderDAOFactory;
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
            FolderDAO folderDAO = folderDAOFactory.createInstance(em);
            
            List<File> newFileList = FolderUtils.getAllFilesInFolder(folder);
            for (final File file : newFileList)
            {
                // If there is already a research file in the database, do not insert another.
                if(researchFileDAO.findByPath(project, file) == null)
                {
                    final ResearchFile researchFile = new ResearchFile(file);
                    researchFile.setProject(project);
                    researchFile.setParentGroup(project.getNewFilesNode());
                    researchFileDAO.createResearchFile(researchFile);
                    folder.getFiles().add(researchFile);
                    folderDAO.updateFolder(folder);
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

    @Override
    public void associateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {

        LOG.info("Assosciating metadata with file. " + file + " " + metadataCategory + " " + metadataValue);

        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final List<MetadataAssociation> existingAssociations = file.getMetadataAssociations();

            final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
    
            boolean addedAssociation = false;
            for (final MetadataAssociation existingAssociation : existingAssociations)
            {
                if (existingAssociation.getMetadataCategory().equals(metadataCategory))
                {
                    if (existingAssociation.getMetadataValues().contains(metadataValue))
                    {
                        // nothing to do!
                        return;
                    }
                    existingAssociation.getMetadataValues().add(metadataValue);
                    metadataAssociationDAO.updateMetadataAssociation(existingAssociation);
                    addedAssociation = true;
                }
            }

            if (!addedAssociation)
            {
                final MetadataAssociation metadataAssociation = new MetadataAssociation(metadataCategory);
                metadataAssociation.getMetadataValues().add(metadataValue);
                metadataAssociationDAO.createMetadataAssociation(metadataAssociation);
                file.getMetadataAssociations().add(metadataAssociation);
            }
            
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
            researchFileDAO.updateResearchFile(file);
        }
        finally
        {
            em.close();
        }
    
    }

    @Override
    public void disassociateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        LOG.info("Disassosciating metadata from file. " + file + " " + metadataCategory + " " + metadataValue);

        final List<MetadataAssociation> existingAssociations = file.getMetadataAssociations();

        for (final MetadataAssociation existingAssociation : existingAssociations)
        {
            if (existingAssociation.getMetadataCategory().equals(metadataCategory))
            {
                if (existingAssociation.getMetadataValues().remove(metadataValue))
                {
                    final EntityManager em = this.entityManagerFactory.createEntityManager();
                    try
                    {
                        final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
                        metadataAssociationDAO.updateMetadataAssociation(existingAssociation);

                        if (existingAssociation.getMetadataValues().isEmpty())
                        {
                            existingAssociations.remove(existingAssociation);

                            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
                            researchFileDAO.updateResearchFile(file);

                            metadataAssociationDAO.removeMetadataAssociation(existingAssociation);
                        }
                        return;
                    }
                    finally
                    {
                        em.close();
                    }
                }
            }
        }
    }

    @Override
    public List<ResearchFile> getResearchFilesWithAssociatedMetadata(MetadataCategory metadataCategory,
            MetadataValue metadataValue)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final String queryJQL = "SELECT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE a.metadataCategory = :category AND :value MEMBER OF a.metadataValues";
            final TypedQuery<ResearchFile> query = em.createQuery(queryJQL, ResearchFile.class);
            query.setParameter("category", metadataCategory);
            query.setParameter("value", metadataValue);
            return query.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public ResearchFile findResearchFileByID(final Long id)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
            return researchFileDAO.findById(id);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void disassociateMultipleMetadataValues(ResearchFile file, MetadataCategory metadataCategory,
            List<MetadataValue> metadataValues)
    {
        for (MetadataValue metadataValue : metadataValues)
        {
            disassociateMetadata(file, metadataCategory, metadataValue);
        }
    }

}
