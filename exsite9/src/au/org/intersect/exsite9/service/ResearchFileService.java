package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.MetadataAssociationDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
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
	private final GroupDAOFactory groupDAOFactory;
	
	public ResearchFileService(final EntityManagerFactory entityManagerFactory,
	                   final ProjectDAOFactory projectDAOFactory,
	                   final ResearchFileDAOFactory researchFileDAOFactory,
	                   final MetadataAssociationDAOFactory metadataAssociationDAOFactory,
	                   final FolderDAOFactory folderDAOFactory,
	                   final GroupDAOFactory groupDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.researchFileDAOFactory = researchFileDAOFactory;
        this.projectDAOFactory = projectDAOFactory;
        this.metadataAssociationDAOFactory = metadataAssociationDAOFactory;
        this.folderDAOFactory = folderDAOFactory;
        this.groupDAOFactory = groupDAOFactory;
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
            if (!folder.getFolder().exists())
            {
                LOG.info("Cannot identify new files in folder '" + folder.getFolder().getAbsolutePath() + "'. It does not exist.");
                return;
            }

            final List<File> newFileList = FolderUtils.getAllFilesInFolder(folder);
            final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
            final FolderDAO folderDAO = folderDAOFactory.createInstance(em);
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
    public void associateMetadata(final ResearchFile file, final MetadataCategory metadataCategory, final MetadataValue metadataValue, final MetadataAttributeValue metadataAttributeValue)
    {

        LOG.info("Assosciating metadata with file. " + file + " " + metadataCategory + " " + metadataValue);

        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final List<MetadataAssociation> existingAssociations = file.getMetadataAssociations();

            final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
    
            boolean addedAssociation = false;

            if (metadataCategory.getType() == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                for (final MetadataAssociation existingAssociation : existingAssociations)
                {
                    if (existingAssociation.getMetadataCategory().equals(metadataCategory))
                    {
                        if (existingAssociation.getMetadataValues().contains(metadataValue))
                        {
                            // nothing to do!
                            return;
                        }
    
                        if (metadataCategory.getType() == MetadataCategoryType.FREETEXT)
                        {
                            existingAssociation.getMetadataValues().clear();
                        }
                        existingAssociation.getMetadataValues().add(metadataValue);
                        existingAssociation.setMetadataAttributeValue(metadataAttributeValue);
                        metadataAssociationDAO.updateMetadataAssociation(existingAssociation);
                        addedAssociation = true;
                    }
                }
            }

            if (metadataCategory.getType() == MetadataCategoryType.FREETEXT || !addedAssociation)
            {
                final MetadataAssociation metadataAssociation = new MetadataAssociation(metadataCategory);
                metadataAssociation.getMetadataValues().add(metadataValue);
                metadataAssociation.setMetadataAttributeValue(metadataAttributeValue);
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
    public void disassociateMetadataAttributeValue(final MetadataCategory metadataCategory, final MetadataAttributeValue metadataAttributeValue)
    {
        final List<ResearchFile> files = getResearchFilesWithAssociatedMetadataAttribute(metadataCategory, metadataAttributeValue);
        for (final ResearchFile file : files)
        {
            final List<MetadataAssociation> associations = file.getMetadataAssociations();
            for (final MetadataAssociation association : associations)
            {
                final MetadataAttributeValue currentMetadataAttributeValue = association.getMetadataAttributeValue();
                if (currentMetadataAttributeValue == null)
                {
                    continue;
                }

                if (association.getMetadataCategory().equals(metadataCategory) && currentMetadataAttributeValue.equals(metadataAttributeValue))
                {
                    association.setMetadataAttributeValue(null);
                    final EntityManager em = this.entityManagerFactory.createEntityManager();
                    try
                    {
                        final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
                        metadataAssociationDAO.updateMetadataAssociation(association);
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
    public List<ResearchFile> getResearchFilesWithAssociatedMetadata(MetadataCategory metadataCategory, MetadataValue metadataValue)
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
    public List<ResearchFile> getResearchFilesWithAssociatedMetadataAttribute(final MetadataCategory metadataCategory, final MetadataAttributeValue metadataAttributeValue)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final String queryJQL = "SELECT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE a.metadataCategory = :category AND a.metadataAttributeValue = :value";
            final TypedQuery<ResearchFile> query = em.createQuery(queryJQL, ResearchFile.class);
            query.setParameter("category", metadataCategory);
            query.setParameter("value", metadataAttributeValue);
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
    public void disassociateMultipleMetadataValues(ResearchFile file, MetadataCategory metadataCategory, List<MetadataValue> metadataValues)
    {
        for (MetadataValue metadataValue : metadataValues)
        {
            disassociateMetadata(file, metadataCategory, metadataValue);
        }
    }

    @Override
    public void updateResearchFile(ResearchFile selectionObject)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
            researchFileDAO.updateResearchFile(selectionObject);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void consolidateSubFolderIntoParentFolder(final Project project, final Folder parentFolder, final Folder subFolder)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            final FolderDAO folderDAO = folderDAOFactory.createInstance(em);
            
            parentFolder.getFiles().addAll(subFolder.getFiles());
            subFolder.getFiles().clear();

            folderDAO.updateFolder(parentFolder);
            folderDAO.updateFolder(subFolder);

            project.getFolders().remove(subFolder);
            projectDAO.updateProject(project);
            folderDAO.removeFolder(subFolder);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void importFolderStructureForProject(Project project, Folder folder)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        
        try
        {
            final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            final FolderDAO folderDAO = folderDAOFactory.createInstance(em);
            
            em.getTransaction().begin();
            
            List<ResearchFile> folderResearchFiles = new ArrayList<ResearchFile>();
            importFolder(em, project, folderResearchFiles, project.getRootNode(), folder.getFolder());
            
            folder.getFiles().addAll(folderResearchFiles);
            folderDAO.createFolder(folder);
            project.getFolders().add(folder);
            projectDAO.updateProject(project);
            
            em.getTransaction().commit();
        }
        catch(Exception e)
        {
            em.getTransaction().rollback();
        }
        finally
        {
            em.close();
        }
    }
    
    private void importFolder(EntityManager em, Project project, List<ResearchFile> folderResearchFiles, Group parentGroup, File folder)
    {
        LOG.debug("Import Folder: " + folder.getName());
        
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        
        // Create group for folder
        Group newGroup = null;
        
        for(Group group : parentGroup.getGroups())
        {
            if(group.getName().equalsIgnoreCase(folder.getName()))
            {
                newGroup = groupDAO.findById(group.getId());
                break;
            }
        }
        
        if(newGroup == null)
        {
            newGroup = new Group(folder.getName());
            newGroup.setParentGroup(parentGroup);
            newGroup.setResearchFileSortDirection(parentGroup.getResearchFileSortDirection());
            newGroup.setResearchFileSortField(parentGroup.getResearchFileSortField());
            parentGroup.getGroups().add(newGroup);
            groupDAO.createGroup(newGroup);
            groupDAO.updateGroup(parentGroup);
        }
            
        // Create Research files for files
        List<File> folderList = new ArrayList<File>(0);
        for(File file : folder.listFiles())
        {
            if(file.isDirectory())
            {
                folderList.add(file);
            }
            else
            {
                boolean createFile = false;
                
                if(researchFileDAO.findByPath(project, file) == null){
                    createFile = true;
                }
                
                if (createFile)
                {
                    ResearchFile researchFile = new ResearchFile(file);
                    researchFile.setProject(project);
                    researchFile.setParentGroup(newGroup);
                    researchFileDAO.createResearchFile(researchFile);
                    folderResearchFiles.add(researchFile);
                    newGroup.getResearchFiles().add(researchFile);
                }
            }
        }

        // Recurse through the folders
        if(! folderList.isEmpty())
        {
            for(File newFolder : folderList)
            {
                importFolder(em, project, folderResearchFiles, newGroup, newFolder);
            }
        }
        
        groupDAO.updateGroup(newGroup);
    }

    @Override
    public void changeAFilesParentFolder(ResearchFile researchFile, long newFolderId)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
            final FolderDAO folderDAO = this.folderDAOFactory.createInstance(em);
            
            Folder folder = researchFileDAO.getParentFolder(researchFile);
            folder.getFiles().remove(researchFile);
            Folder newFolder = folderDAO.findById(newFolderId);
            newFolder.getFiles().add(researchFile);
            folderDAO.updateFolder(folder);
            folderDAO.updateFolder(newFolder);

        }
        finally
        {
            em.close();
        }
    }
}
