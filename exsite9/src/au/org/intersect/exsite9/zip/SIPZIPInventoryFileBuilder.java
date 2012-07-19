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
            
            fileDetails.append(researchFile.getFile().getName());
            
            while(!parent.equals(project.getRootNode()))
            {
                //insert parent name at the front of the path.
                fileDetails.insert(0, parent.getName() + "/");              
                parent = parent.getParentGroup();
            }
            
            fileDetails.append(" | ");                      
            fileDetails.append(Long.toString(researchFile.getFile().length()) + " Bytes");
            if (!researchFile.getFile().exists())
            {
                fileDetails.append(" (MISSING)");
            }
           
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
}
