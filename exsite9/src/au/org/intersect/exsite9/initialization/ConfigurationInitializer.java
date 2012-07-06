/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.initialization;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import au.org.intersect.exsite9.Activator;

/**
 * Configures default values for configuration items.
 */
public class ConfigurationInitializer extends AbstractPreferenceInitializer
{
    /**
     * The key used to represent the folder reload interval (in minutes).
     */
    public static final String FOLDER_RELOAD_INTERVAL_KEY = "exsite9.folder.refresh.interval";
    public static final int FOLDER_RELOAD_INTERVAL_DEFAULT = 0;

    /**
     * 
     */
    public ConfigurationInitializer()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void initializeDefaultPreferences()
    {
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(FOLDER_RELOAD_INTERVAL_KEY, FOLDER_RELOAD_INTERVAL_DEFAULT);
    }
}
