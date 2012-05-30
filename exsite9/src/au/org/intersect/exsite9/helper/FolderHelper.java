package au.org.intersect.exsite9.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.ResearchFile;

public class FolderHelper
{
    /**
     * Recursively looks for files in the folder & it's sub folders that are new since the last time
     * it checked.
     * Returns an empty list if there are no files or this is not a folder.
     * Time resolution of ext3 & hfs+ is 1 second so when we store the time of the
     * last check we round it down. 
     * @return A list of the files identified in the folder since the last time the folder was checked
     */
    public static List<ResearchFile> identifyNewFiles(Folder folder)
    {
    	long startTimeInMillis = Calendar.getInstance().getTimeInMillis();
    	
        List<ResearchFile> newFileList = new ArrayList<ResearchFile>(0);
        
        IOFileFilter ageFilter = new AgeFileFilter(folder.getLastCheckTimeInMillis(),false);
        
        try
        {
            List<File> allFiles = (List<File>) FileUtils.listFiles(new File(folder.getPath()), ageFilter, TrueFileFilter.INSTANCE);
        
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
        
        // The time resolution of the various supported file systems is an issue here.
        folder.setLastCheckTimeInMillis(startTimeInMillis - 1000);
        
        return newFileList;
    }

}
