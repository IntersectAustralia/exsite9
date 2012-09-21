/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A Wizard Dialog that sets focus to a custom button i.e. not the default.
 */
public final class CustomButtonFocusWizardDialog extends WizardDialog
{
    private final int buttonID;

    /**
     * Constructor
     * @param parentShell
     * @param wizard
     * @param buttonID The ID of the button to be in focus. Choose from {@link IDialogConstants}
     */
    public CustomButtonFocusWizardDialog(final Shell parentShell, final IWizard wizard, final int buttonID)
    {
        super(parentShell, wizard);
        this.buttonID = buttonID;
    }

    @Override
    public void updateButtons()
    {
        super.updateButtons();
        getShell().setDefaultButton(getButton(this.buttonID));
    }
}
