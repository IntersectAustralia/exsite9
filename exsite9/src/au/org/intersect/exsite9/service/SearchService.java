package au.org.intersect.exsite9.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.ResearchFileSearchDefinition;

public class SearchService implements ISearchService
{
    private static final String METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE f.project = :project AND a.metadataAttributeValue.value LIKE :searchTerm";
    private static final String METADATA_CATEGORY_VALUE_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a JOIN a.metadataValues va WHERE f.project = :project AND a.metadataCategory.type = :categoryType AND va.value LIKE :searchTerm";
    private static final String METADATA_CATEGORY_NAME_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE f.project = :project AND a.metadataCategory.name LIKE :searchTerm";
    private final EntityManagerFactory entityManagerFactory;

    public SearchService(EntityManagerFactory emf)
    {
        this.entityManagerFactory = emf;
    }

    @Override
    public List<ResearchFile> getResearchFilesUsingSearchTerm(String searchTerm,
            ResearchFileSearchDefinition selectedField, Project currentProject)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final TypedQuery<ResearchFile> query;
            StringBuilder sb = new StringBuilder("%");
            sb.append(searchTerm);
            sb.append("%");

            switch (selectedField)
            {
                case ALL_FIELDS:
                    //Couldn't get the query below to result all the expected results so had to HACK HACK HACK.
                    //query = em.createQuery("SELECT DISTINCT f FROM ResearchFile f LEFT JOIN f.metadataAssociations a LEFT JOIN a.metadataValues va WHERE f.project = :project AND (a.metadataCategory.name LIKE :searchTerm OR va.value LIKE :searchTerm OR a.metadataAttributeValue.value LIKE :searchTerm)", ResearchFile.class);
                    query = em.createQuery(METADATA_CATEGORY_NAME_QUERY_STRING, ResearchFile.class);
                    query.setParameter("searchTerm", sb.toString());
                    query.setParameter("project", currentProject);
                    TypedQuery<ResearchFile> query2 = em.createQuery("SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a JOIN a.metadataValues va WHERE f.project = :project AND va.value LIKE :searchTerm", ResearchFile.class);
                    query2.setParameter("searchTerm", sb.toString());
                    query2.setParameter("project", currentProject);
                    TypedQuery<ResearchFile> query3 = em.createQuery(METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, ResearchFile.class);
                    query3.setParameter("searchTerm", sb.toString());
                    query3.setParameter("project", currentProject);
                    
                    List<ResearchFile> resultsFromCategoryName = query.getResultList();
                    List<ResearchFile> resultsFromValue = query2.getResultList();
                    List<ResearchFile> resultsFromAttributeName = query3.getResultList();
                    
                    List<ResearchFile> allResults = resultsFromCategoryName;
                    allResults.addAll(resultsFromValue);
                    allResults.addAll(resultsFromAttributeName);
                    
                    Set<ResearchFile> resultsWithNoDuplicates = new HashSet<ResearchFile>(allResults);
                    return Arrays.asList(resultsWithNoDuplicates.toArray(new ResearchFile[resultsWithNoDuplicates.size()]));
                    
                case METADATA_CATEGORY_NAME:
                    query = em.createQuery(METADATA_CATEGORY_NAME_QUERY_STRING, ResearchFile.class);
                    break;

                case METADATA_VALUE:
                case FREETEXT_METADATA_VALUE:
                    query = em.createQuery(METADATA_CATEGORY_VALUE_QUERY_STRING, ResearchFile.class);
                    query.setParameter("categoryType", selectedField == ResearchFileSearchDefinition.FREETEXT_METADATA_VALUE ? MetadataCategoryType.FREETEXT : MetadataCategoryType.CONTROLLED_VOCABULARY);
                    break;
                    
                case METADATA_ATTRIBUTE:
                    query = em.createQuery(METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, ResearchFile.class);
                    break;

                default:
                    throw new IllegalArgumentException("Unkown search field");
            }
            query.setParameter("searchTerm", sb.toString());
            query.setParameter("project", currentProject);

            return query.getResultList();
        }
        finally
        {
            em.close();
        }
    }

}
