package au.org.intersect.exsite9.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.xml.SIPXMLBuilder;

public final class SIPZIPBuilder
{
    public static void buildZIP(final Project project, final List<Group> selectedGroups, final SubmissionPackage submissionPackage, final File destinationFile) throws IOException
    {
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destinationFile, false));
        try
        {
            for (final Group group : project.getRootNode().getGroups())
            {
                if (selectedGroups.contains(group))
                {
                    createDirForGroup(group, zipOutputStream, "", selectedGroups, submissionPackage);
                }
            }
            copyResearchFiles(project.getRootNode(), zipOutputStream, "", submissionPackage);

            // Put the SIP XML in place.
            final String xml = SIPXMLBuilder.buildXML(project, selectedGroups, submissionPackage.getResearchFiles());
            final ZipEntry sipXMLZipEntry = new ZipEntry(project.getName() + ".xml");
            zipOutputStream.putNextEntry(sipXMLZipEntry);
            final InputStream is = new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8));
            try
            {
                ByteStreams.copy(is, zipOutputStream);
            }
            finally
            {
                zipOutputStream.closeEntry();
                is.close();
            }

            // Also put the SIP Inventory in place.
        }
        finally
        {
            zipOutputStream.close();
        }
    }

    private static void createDirForGroup(final Group group, final ZipOutputStream zipOutputStream, final String parentPath, final List<Group> selectedGroups, final SubmissionPackage submissionPackage) throws IOException
    {
        final String groupPath = parentPath + group.getName() + "/";
        final ZipEntry groupDirZipEntry = new ZipEntry(groupPath);
        zipOutputStream.putNextEntry(groupDirZipEntry);
        zipOutputStream.closeEntry();

        copyResearchFiles(group, zipOutputStream, groupPath, submissionPackage);

        for (final Group child : group.getGroups())
        {
            if (selectedGroups.contains(child))
            {
                createDirForGroup(child, zipOutputStream, groupPath, selectedGroups, submissionPackage);
            }
        }
    }

    private static void copyResearchFiles(final Group group, final ZipOutputStream zipOutputStream, final String parentPath, final SubmissionPackage submissionPackage) throws IOException
    {
        for (final ResearchFile researchFile : group.getResearchFiles())
        {
            if (submissionPackage.getResearchFiles().contains(researchFile))
            {
                final ZipEntry zipEntry = new ZipEntry(parentPath + researchFile.getFile().getName());
                zipOutputStream.putNextEntry(zipEntry);
                final InputStream is = new FileInputStream(researchFile.getFile());
                try
                {
                    ByteStreams.copy(is, zipOutputStream);
                }
                finally
                {
                    zipOutputStream.closeEntry();
                    is.close();
                }
            }
        }
    }
}
