/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

/**
 * The Wizard used to create a new project
 */
public final class NewProjectWizard extends Wizard
{

    public NewProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages()
    {
        final WizardPage page1 = new NewProjectWizardPage1();
        addPage(page1);
    }

    @Override
    public boolean performFinish()
    {
        System.out.println("FINISHED!");
        return true;
    }

}
