/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.configpages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.initialization.ConfigurationInitializer;

/**
 * The configuration page.
 * Used to get/set configuration of the application from the user.
 */
public class ConfigPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

    @Override
    public void init(final IWorkbench workbench)
    {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Configure ExSite9");
    }

    @Override
    protected void createFieldEditors()
    {
        final IntegerFieldEditor refreshIntervalFieldEditor = new IntegerFieldEditor(ConfigurationInitializer.FOLDER_RELOAD_INTERVAL_KEY,
                "&Folder refresh interval (minutes):", getFieldEditorParent(), 3);
        refreshIntervalFieldEditor.setEmptyStringAllowed(false);
        refreshIntervalFieldEditor.setValidRange(0, 500);
        addField(refreshIntervalFieldEditor);
    }
}
