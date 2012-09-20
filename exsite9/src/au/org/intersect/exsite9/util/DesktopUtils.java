package au.org.intersect.exsite9.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Utilities for the Desktop.
 */
public final class DesktopUtils
{
    /**
     * Opens the provided file with it's default application configured on the system.
     * Does nothing if it is not supported on the current platform.
     * @param file The file to open.
     * @throws IOException If the file could not be opened.
     */
    public static void openWithDefaultApplication(final File file) throws IOException
    {
        if (!Desktop.isDesktopSupported())
        {
            return;
        }

        try
        {
            Desktop.getDesktop().open(file);
        }
        catch (final IOException e)
        {
            // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6780505
            Desktop.getDesktop().browse(file.toURI());
        }
    }
}
