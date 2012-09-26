/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import au.org.intersect.exsite9.domain.Folder;

/**
 * Performs utilities on watched folders {@link Folder}
 */
public final class FolderUtils
{
    private FolderUtils()
    {
        // No instances please!
    }

    /**
     * Recursively looks for files in the folder & it's sub folders that are new since the last time
     * it checked.
     * Returns an empty list if there are no files or this is not a folder.
     * Time resolution of ext3 & hfs+ is 1 second so when we store the time of the
     * last check we round it down. 
     * @return A list of the files identified in the folder since the last time the folder was checked
     */
    public static List<File> identifyNewFiles(final Folder folder)
    {
    	final IOFileFilter ageFilter = new AgeFileFilter(folder.getLastCheckTimeInMillis(),false);
        return listFiles(folder, ageFilter, TrueFileFilter.INSTANCE);
    }

    /**
     * Recursively looks for files in the folder & it's sub folders.
     * Returns an empty list if there are no files or this is not a folder.
     * 
     * @return A list of the files in the folder
     */
    public static List<File> getAllFilesInFolder(final Folder folder)
    {
        return listFiles(folder, HiddenFileFilter.VISIBLE, TrueFileFilter.INSTANCE);
    }

    private static List<File> listFiles(final Folder folder, final IOFileFilter fileFilter, final IOFileFilter dirFilter)
    {
        final List<File> newFileList = new ArrayList<File>(FileUtils.listFiles(folder.getFolder(), fileFilter, dirFilter));
        folder.setLastCheckTimeInMillis(System.currentTimeMillis());
        return newFileList;
    }
}
