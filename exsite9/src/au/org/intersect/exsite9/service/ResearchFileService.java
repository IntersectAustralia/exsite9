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

                    if (metadataCategory.getType() == MetadataCategoryType.FREETEXT)
                    {
                        existingAssociation.getMetadataValues().clear();
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
            
            folderDAO.createFolder(folder);
            
            em.getTransaction().begin();
            
            importFolder(em, project, folder, project.getRootNode(), folder);
            
            project.getFolders().add(folder);
            projectDAO.updateProject(project);
        }
        catch(Exception e)
        {
            em.getTransaction().rollback();
        }
        finally
        {
            em.getTransaction().commit();
            em.close();
        }
    }
    
    private void importFolder(EntityManager em, Project project, Folder parentFolder, Group parentGroup, Folder folder)
    {
        LOG.debug("Import Folder: " + folder.getFolder().getName());
        
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        
        // Create group for folder
        Group newGroup = null;
        
        for(Group group : parentGroup.getGroups())
        {
            if(group.getName().equalsIgnoreCase(folder.getFolder().getName()))
            {
                newGroup = group;
                break;
            }
        }
        
        if(newGroup == null)
        {
            newGroup = new Group(folder.getFolder().getName());
            newGroup.setParentGroup(parentGroup);
            parentGroup.getGroups().add(newGroup);
            groupDAO.createGroup(newGroup);
            groupDAO.updateGroup(parentGroup);
        }
            
        // Create Research files for files
        List<File> folderList = new ArrayList<File>(0);
        for(File file : folder.getFolder().listFiles())
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
                
                //for(ResearchFile researchFile : newGroup.getResearchFiles())
                //{
                //    if(researchFile.getFile().equals(file))
                //    {
                //        createFile = false;
                //        break;
                //    }
                //}
                
                if (createFile)
                {
                    ResearchFile researchFile = new ResearchFile(file);
                    researchFile.setProject(project);
                    researchFile.setParentGroup(newGroup);
                    researchFileDAO.createResearchFile(researchFile);
                    parentFolder.getFiles().add(researchFile);
                    newGroup.getResearchFiles().add(researchFile);
                }
            }
        }
        groupDAO.updateGroup(newGroup);

        // Recurse through the folders
        if(! folderList.isEmpty())
        {
            for(File folderOnDisk : folderList)
            {
                Folder newFolder = new Folder(folderOnDisk);
                importFolder(em, project, parentFolder, newGroup, newFolder);
            }
        }
        
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
