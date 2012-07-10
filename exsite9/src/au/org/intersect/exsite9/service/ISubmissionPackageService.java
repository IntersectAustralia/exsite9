/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;

/**
 * Service to perform actions with {@link SubmissionPackage}s
 */
public interface ISubmissionPackageService
{
    /**
     * Creates a Submission Package.
     * @param project The project the submission package belongs to.
     * @param name The name of the submission package.
     * @param description The description of the submission package.
     * @param researchFiles The research files contained in the submission package.
     * @return The created submission package.
     */
    SubmissionPackage createSubmissionPackage(final Project project, final String name, final String description, final List<ResearchFile> researchFiles);

    /**
     * Finds a submission package by ID.
     * @param id The ID of the submission package to find.
     * @return The submission package.
     */
    SubmissionPackage findSubmissionPackageById(final Long id);

    /**
     * Updates a Submission Package.
     * @param submissionPackage The submission package to update.
     * @param name The new name of the submission package.
     * @param description The new description of the submission package.
     * @param researchFiles The new set of research files for the submission package.
     * @return The updated submission package.
     */
    SubmissionPackage updateSubmissionPackage(final SubmissionPackage submissionPackage, final String name, final String description, final List<ResearchFile> researchFiles);

    /**
     * Deletes a Submission Package.
     * @param submissionPackage The submission package to delete.
     */
    void deleteSubmissionPackage(final SubmissionPackage submissionPackage);
    
    /**
     * Builds the xml for a submission package
     * @param project The project
     * @param submissionPackage The submission package
     * @return
     */
    String buildXMLForSubmissionPackage(final Project project, final SubmissionPackage submissionPackage);

    /**
     * Builds the ZIP for a submission package
     * @param project The project
     * @param submissionPackage The submission package
     * @param fileToWrite The file to write the ZIP to.
     */
    void buildZIPForSubmissionPackage(final Project project, final SubmissionPackage submissionPackage, final File fileToWrite) throws IOException;
}
