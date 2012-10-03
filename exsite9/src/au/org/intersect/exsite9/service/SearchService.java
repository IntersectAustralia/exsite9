/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SearchDefinition;

/**
 * A service for forming and executing a search on the database
 */
public class SearchService implements ISearchService
{
    private static final String FILE_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE f.project = :project AND LOWER(a.metadataAttributeValue.value) LIKE LOWER(:searchTerm)";
    private static final String FILE_METADATA_CATEGORY_VALUE_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a JOIN a.metadataValues va WHERE f.project = :project AND a.metadataCategory.type = :categoryType AND LOWER(va.value) LIKE LOWER(:searchTerm)";
    private static final String FILE_METADATA_VALUE_IRRELEVANT_OF_VALUE_TYPE_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a JOIN a.metadataValues va WHERE f.project = :project AND LOWER(va.value) LIKE LOWER(:searchTerm)";
    private static final String FILE_METADATA_CATEGORY_NAME_QUERY_STRING = "SELECT DISTINCT f FROM ResearchFile f JOIN f.metadataAssociations a WHERE f.project = :project AND LOWER(a.metadataCategory.name) LIKE LOWER(:searchTerm)";
    private static final String GROUP_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING = "SELECT DISTINCT g FROM Group g JOIN g.metadataAssociations a WHERE g.project = :project AND LOWER(a.metadataAttributeValue.value) LIKE LOWER(:searchTerm)";
    private static final String GROUP_METADATA_CATEGORY_VALUE_QUERY_STRING = "SELECT DISTINCT g FROM Group g JOIN g.metadataAssociations a JOIN a.metadataValues va WHERE g.project = :project AND a.metadataCategory.type = :categoryType AND LOWER(va.value) LIKE LOWER(:searchTerm)";
    private static final String GROUP_METADATA_VALUE_IRRELEVANT_OF_VALUE_TYPE_QUERY_STRING = "SELECT DISTINCT g FROM Group g JOIN g.metadataAssociations a JOIN a.metadataValues va WHERE g.project = :project AND LOWER(va.value) LIKE LOWER(:searchTerm)";
    private static final String GROUP_METADATA_CATEGORY_NAME_QUERY_STRING = "SELECT DISTINCT g FROM Group g JOIN g.metadataAssociations a WHERE g.project = :project AND LOWER(a.metadataCategory.name) LIKE LOWER(:searchTerm)";
    private final EntityManagerFactory entityManagerFactory;

    public SearchService(EntityManagerFactory emf)
    {
        this.entityManagerFactory = emf;
    }

    @Override
    public List<Object> getSearchResults(String searchTerm, SearchDefinition selectedField, Project currentProject)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final TypedQuery<ResearchFile> fileQuery;
            final TypedQuery<Group> groupQuery;
            StringBuilder sb = new StringBuilder("%");
            sb.append(searchTerm);
            sb.append("%");

            switch (selectedField)
            {
                case ALL_METADATA_FIELDS:
                    // Couldn't get the query below to result all the expected results so had to HACK HACK HACK.
                    // query =
                    // em.createQuery("SELECT DISTINCT f FROM ResearchFile f LEFT JOIN f.metadataAssociations a LEFT JOIN a.metadataValues va WHERE f.project = :project AND (a.metadataCategory.name LIKE :searchTerm OR va.value LIKE :searchTerm OR a.metadataAttributeValue.value LIKE :searchTerm)",
                    // ResearchFile.class);
                    fileQuery = em.createQuery(FILE_METADATA_CATEGORY_NAME_QUERY_STRING, ResearchFile.class);
                    fileQuery.setParameter("searchTerm", sb.toString());
                    fileQuery.setParameter("project", currentProject);
                    TypedQuery<ResearchFile> fileQuery2 = em.createQuery(FILE_METADATA_VALUE_IRRELEVANT_OF_VALUE_TYPE_QUERY_STRING, ResearchFile.class);
                    fileQuery2.setParameter("searchTerm", sb.toString());
                    fileQuery2.setParameter("project", currentProject);
                    TypedQuery<ResearchFile> fileQuery3 = em.createQuery(FILE_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, ResearchFile.class);
                    fileQuery3.setParameter("searchTerm", sb.toString());
                    fileQuery3.setParameter("project", currentProject);

                    List<ResearchFile> resultsFromFileCategoryName = fileQuery.getResultList();
                    List<ResearchFile> resultsFromFileValue = fileQuery2.getResultList();
                    List<ResearchFile> resultsFromFileAttributeName = fileQuery3.getResultList();

                    List<ResearchFile> allFileResults = resultsFromFileCategoryName;
                    allFileResults.addAll(resultsFromFileValue);
                    allFileResults.addAll(resultsFromFileAttributeName);

                    Set<ResearchFile> resultsWithNoDuplicates = new HashSet<ResearchFile>(allFileResults);
                    List<ResearchFile> fileResultsWithNoDuplicationsAsList = Arrays.asList(resultsWithNoDuplicates
                            .toArray(new ResearchFile[resultsWithNoDuplicates.size()]));

                    groupQuery = em.createQuery(GROUP_METADATA_CATEGORY_NAME_QUERY_STRING, Group.class);
                    groupQuery.setParameter("searchTerm", sb.toString());
                    groupQuery.setParameter("project", currentProject);
                    TypedQuery<Group> groupQuery2 = em.createQuery(GROUP_METADATA_VALUE_IRRELEVANT_OF_VALUE_TYPE_QUERY_STRING, Group.class);
                    groupQuery2.setParameter("searchTerm", sb.toString());
                    groupQuery2.setParameter("project", currentProject);
                    TypedQuery<Group> groupQuery3 = em.createQuery(GROUP_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, Group.class);
                    groupQuery3.setParameter("searchTerm", sb.toString());
                    groupQuery3.setParameter("project", currentProject);

                    List<Group> resultsFromGroupCategoryName = groupQuery.getResultList();
                    List<Group> resultsFromGroupValue = groupQuery2.getResultList();
                    List<Group> resultsFromGroupAttributeName = groupQuery3.getResultList();

                    List<Group> allGroupResults = resultsFromGroupCategoryName;
                    allGroupResults.addAll(resultsFromGroupValue);
                    allGroupResults.addAll(resultsFromGroupAttributeName);

                    Set<Group> groupResultsWithNoDuplicates = new HashSet<Group>(allGroupResults);
                    List<Group> groupResultsWithNoDuplicationsAsList = Arrays.asList(groupResultsWithNoDuplicates.toArray(new Group[groupResultsWithNoDuplicates
                            .size()]));
                    
                    List<Object> combinedAllFieldResults = new ArrayList<Object>();
                    combinedAllFieldResults.addAll(groupResultsWithNoDuplicationsAsList);
                    combinedAllFieldResults.addAll(fileResultsWithNoDuplicationsAsList);
                    return combinedAllFieldResults;
                case METADATA_CATEGORY_NAME:
                    groupQuery = em.createQuery(GROUP_METADATA_CATEGORY_NAME_QUERY_STRING, Group.class);
                    fileQuery = em.createQuery(FILE_METADATA_CATEGORY_NAME_QUERY_STRING, ResearchFile.class);
                    break;

                case METADATA_VALUE:
                case FREETEXT_METADATA_VALUE:
                    MetadataCategoryType metadataCategoryType = selectedField == SearchDefinition.FREETEXT_METADATA_VALUE ? MetadataCategoryType.FREETEXT
                            : MetadataCategoryType.CONTROLLED_VOCABULARY;
                    groupQuery = em.createQuery(GROUP_METADATA_CATEGORY_VALUE_QUERY_STRING, Group.class);
                    groupQuery.setParameter("categoryType", metadataCategoryType);
                    fileQuery = em.createQuery(FILE_METADATA_CATEGORY_VALUE_QUERY_STRING, ResearchFile.class);
                    fileQuery.setParameter("categoryType", metadataCategoryType);
                    break;

                case METADATA_ATTRIBUTE:
                    groupQuery = em.createQuery(GROUP_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, Group.class);
                    fileQuery = em.createQuery(FILE_METADATA_CATEGORY_ATTRIBUTE_QUERY_STRING, ResearchFile.class);
                    break;

                default:
                    throw new IllegalArgumentException("Unkown search field");
            }
            groupQuery.setParameter("searchTerm", sb.toString());
            groupQuery.setParameter("project", currentProject);
            fileQuery.setParameter("searchTerm", sb.toString());
            fileQuery.setParameter("project", currentProject);

            List<Group> groupResultList = groupQuery.getResultList();
            List<ResearchFile> fileResultList = fileQuery.getResultList();
            List<Object> combinedResultsList = new ArrayList<Object>();
            combinedResultsList.addAll(groupResultList);
            combinedResultsList.addAll(fileResultList);
            return combinedResultsList;
        }
        finally
        {
            em.close();
        }
    }
}
