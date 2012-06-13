/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import au.org.intersect.exsite9.logging.StdOutErrLog;

/**
 *
 */
public final class Activator extends AbstractUIPlugin
{
    private static final String LOG4J_PATTERN = "%p [%d{yyyy-MM-dd HH:mm:ss,SSS}] %C - %m%n";

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
        final Logger rootLogger = Logger.getRootLogger();
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout(LOG4J_PATTERN)));

        final String workspaceDir = Platform.getInstallLocation().getURL().getPath();
        final String logFile = workspaceDir + "/log/exsite9.log";
        final RollingFileAppender fileAppender = new RollingFileAppender(new PatternLayout(LOG4J_PATTERN), logFile, true);
        fileAppender.setImmediateFlush(true);
        fileAppender.setMaxBackupIndex(10);
        fileAppender.setMaxFileSize("10MB");
        rootLogger.addAppender(fileAppender);

        StdOutErrLog.redirect();
        rootLogger.info("Starting ExSite9");

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
