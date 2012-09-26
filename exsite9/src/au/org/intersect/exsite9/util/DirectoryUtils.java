/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import java.util.regex.Pattern;

/**
 * Provides utilities for Directories.
 */
public final class DirectoryUtils
{
    private DirectoryUtils()
    {
        // No instances please!
    }

    /**
     * Regex to check for illegal characters in directory names.
     * Illegal characters are: \ / : * " ? < > |
     */
    private static final Pattern ILLEGAL_DIRNAME_REGEX = Pattern.compile("^.*[\\\\/:\\*\"\\?<>\\|].*");

    /**
     * Determines if the provided String is a valid directory name.
     * It will ensure the dirName is valid on Linux, Mac and Windows.
     * @param dirName The String to check.
     * @return {@code true} if the String can be used as a valid directory name.
     */
    public static boolean isValidDirectoryName(final String dirName)
    {
        return !ILLEGAL_DIRNAME_REGEX.matcher(dirName).matches();
    }
}
