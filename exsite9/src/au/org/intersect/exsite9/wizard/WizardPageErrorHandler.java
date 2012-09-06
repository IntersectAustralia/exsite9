/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;

/**
 * Displays error messages on wizard pages.
 */
public final class WizardPageErrorHandler implements IFieldErrorMessageHandler
{
    private final WizardPage wizardPage;

    public WizardPageErrorHandler(final WizardPage wizardPage)
    {
        this.wizardPage = wizardPage;
    }

    @Override
    public void handleErrorMessage(final String message, final String input)
    {
        this.wizardPage.setMessage(null, DialogPage.WARNING);
        this.wizardPage.setErrorMessage(message);
    }

    @Override
    public void handleWarningMessage(final String message, final String input)
    {
        this.wizardPage.setErrorMessage(null);
        this.wizardPage.setMessage(message, DialogPage.WARNING);
    }
    
    
    @Override
    public void clearMessage()
    {
        this.wizardPage.setErrorMessage(null);
        this.wizardPage.setMessage(null);
    }
}
