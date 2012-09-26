/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import au.org.intersect.exsite9.logging.StdOutErrLog;

/**
 * Activates the application
 */
public final class Activator extends AbstractUIPlugin
{
    // The plug-in ID
    public static final String PLUGIN_ID = "au.org.intersect.exsite9"; //$NON-NLS-1$

    // The shared instance
    private static Activator PLUGIN;

    /**
     * The constructor
     */
    public Activator()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void start(final BundleContext context) throws Exception
    {
        // Configure log4j
        final String workspaceDir = Platform.getInstallLocation().getURL().getPath();
        final File configurationDir = new File(workspaceDir, "configuration");
        final File log4jxmlFile = new File(configurationDir, "log4j.xml");

        // Because the log4j.xml refers to workspace.dir
        System.setProperty("workspace.dir", workspaceDir);
        DOMConfigurator.configure(log4jxmlFile.getAbsolutePath());

        final Logger rootLogger = Logger.getRootLogger();
        StdOutErrLog.redirect();
        rootLogger.info(" *** Starting ExSite9 ***");
        rootLogger.info("java.home: " + System.getProperty("java.home"));
        rootLogger.info("java.vendor: " + System.getProperty("java.vendor"));
        rootLogger.info("java.version: " + System.getProperty("java.version"));
        rootLogger.info("os.arch: " + System.getProperty("os.arch"));
        rootLogger.info("os.name: " + System.getProperty("os.name"));
        rootLogger.info("os.version: " + System.getProperty("os.version"));
        rootLogger.info("workspace.dir: " + System.getProperty("workspace.dir"));

        super.start(context);
        PLUGIN = this;
    }

    /**
     * {@inheritDoc}
     */
    public void stop(final BundleContext context) throws Exception
    {
        PLUGIN = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return PLUGIN;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
