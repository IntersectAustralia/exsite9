package au.org.intersect.exsite9.zip;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public class SIPZIPInventoryBuilderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String EMPTY_STRING = "";

    @Test
    public void buildInventoryFileUnitTest() throws IOException
    {             
        final Project project = new Project(new ProjectFieldsDTO("proj1", EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
        
        final File file1 = new File("someResearchFile.txt");
        final File file2 = new File("someOtherResearchFile.txt");
        final File file3 = new File("yetAnotherResearchFile.txt");
        
        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);
        //rf3 should not appear in the file as we do not add it too the package
        final ResearchFile rf3 = new ResearchFile(file3);
        
        project.getRootNode().setId(1L);
        
        final Group group1 = new Group("group1");
        group1.setId(2L);
        group1.setParentGroup(project.getRootNode());
        
        rf1.setParentGroup(group1);
        
        final Group subGroup1 = new Group("subGroup1");
        subGroup1.setId(3L);

        rf2.setParentGroup(subGroup1);
        subGroup1.setParentGroup(group1);

        project.getRootNode().getGroups().add(group1);
        rf3.setParentGroup(project.getRootNode());
        
        SubmissionPackage subPack = new SubmissionPackage();
        subPack.setName("package1");
        subPack.getResearchFiles().add(rf1);
        subPack.getResearchFiles().add(rf2);
        
        final String resultString = SIPZIPInventoryFileBuilder.buildInventoryFile(project, subPack);
        
        final String expectedString = "group1/someResearchFile.txt | 0 Bytes (MISSING) | 01/01/1970 10:00:00" + NEW_LINE + NEW_LINE +
                "group1/subGroup1/someOtherResearchFile.txt | 0 Bytes (MISSING) | 01/01/1970 10:00:00" + NEW_LINE + NEW_LINE;
        
        assertEquals(expectedString, resultString);
    }

}
