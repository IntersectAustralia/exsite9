/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.importmetadataschema;


import au.org.intersect.exsite9.domain.Project;

/**
 * Wizard used to import a Metadata Schema.
 */
public final class ImportMetadataSchemaWizard extends MetadataSchemaEditingWizard
{
    public ImportMetadataSchemaWizard(final Project selectedProject)
    {
        super(selectedProject, "Modify Metadata Schema");
        setNeedsProgressMonitor(true);
    }
}
