/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * Represents a folder on the researcher's desktop that contains research data files
 */
@Entity
public final class Folder
{
    private final String name;
    private final String path;
    private long lastCheckTimeInMillis = 0L;
    
    public Folder(String name, String path)
    {
        this.name = name;
        this.path = path;
    }

    public Folder(File fileOnDisk)
    {
        this.name = fileOnDisk.getName();
        this.path = fileOnDisk.getAbsolutePath();
    }
    
    /**
     * Recursively looks for files in the folder & it's sub folders that are new since the last time
     * it checked.
     * Returns an empty list if there are no files or this is not a folder.
     * @return A list of the files identified in the folder since the last time the folder was checked
     */
    public List<ResearchFile> identifyNewFiles()
    {
        List<ResearchFile> newFileList = new ArrayList<ResearchFile>(0);
        long startTimeInMillis = Calendar.getInstance().getTimeInMillis();
        
        IOFileFilter ageFilter = new AgeFileFilter(this.lastCheckTimeInMillis);
        
        try
        {
            List<File> allFiles = (List<File>) FileUtils.listFiles(new File(this.path), ageFilter, TrueFileFilter.INSTANCE);
        
            for(File file : allFiles)
            {
                newFileList.add(new ResearchFile(file));
            }
        }
        catch(IllegalArgumentException iae)
        {
            // If this wasn't a folder we can continue
            // TODO: Think about logging
        }
        
        this.lastCheckTimeInMillis = startTimeInMillis;
        
        return newFileList;
    }

    public long getLastCheckTimeInMillis()
    {
        return lastCheckTimeInMillis;
    }

    public void setLastCheckTimeInMillis(long lastCheckTimeInMillis)
    {
        this.lastCheckTimeInMillis = lastCheckTimeInMillis;
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }
}
