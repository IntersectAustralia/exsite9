package au.org.intersect.exsite9.zip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;

public class SIPZIPInventoryFileBuilder
{
    
    
    public static String buildInventoryFile(final Project project, final SubmissionPackage submissionPackage)
    {        
        StringBuffer result = new StringBuffer();
        final List<ResearchFile> selectedFiles = submissionPackage.getResearchFiles();
        
        for (ResearchFile researchFile : selectedFiles)
        {
            StringBuffer fileDetails = new StringBuffer();
            
            Group parent = researchFile.getParentGroup();
            
            fileDetails.append(parent.getName() + "/");
            fileDetails.append(researchFile.getFile().getName());
            
            while(!parent.equals(project.getRootNode()))
            {
                parent = parent.getParentGroup();
                //insert parent name at the front of the path.
                fileDetails.insert(0, parent.getName() + "/");              
            }
            
            fileDetails.append(" | ");            
            long fileSize = researchFile.getFile().length();            
            fileDetails.append(formatByteSize(fileSize));
           
            fileDetails.append(" | ");
            long lastModifiedStamp = researchFile.getFile().lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date dateStamp = new Date(lastModifiedStamp);
            fileDetails.append(dateFormat.format(dateStamp));
                         
            fileDetails.append(System.getProperty("line.separator"));
            fileDetails.append(System.getProperty("line.separator"));
            
            result.append(fileDetails.toString());
        }
        
        return result.toString();
    }
    
    private static String formatByteSize(long bytes)
    {
        int units = 1000;
        
        if (bytes < units)
        {
            return bytes + " B";
        }
        
        int unitResult = (int) (Math.log(bytes) / Math.log(units));
        String size = "KMGT".charAt(unitResult-1) + "B";
        return String.format("%.1f %s", bytes / Math.pow(units, unitResult), size);        
    }

}
