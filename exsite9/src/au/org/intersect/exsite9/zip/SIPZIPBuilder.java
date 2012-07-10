package au.org.intersect.exsite9.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

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
        final ZipArchiveOutputStream zipArchiveOuputStream = new ZipArchiveOutputStream(destinationFile);
        zipArchiveOuputStream.setUseZip64(Zip64Mode.AsNeeded);

        try
        {
            for (final Group group : project.getRootNode().getGroups())
            {
                if (selectedGroups.contains(group))
                {
                    createDirForGroup(group, zipArchiveOuputStream, "", selectedGroups, submissionPackage);
                }
            }
            copyResearchFiles(project.getRootNode(), zipArchiveOuputStream, "", submissionPackage);

            // Put the SIP XML in place.
            final String xml = SIPXMLBuilder.buildXML(project, selectedGroups, submissionPackage, true);
            
            final ZipArchiveEntry sipXMLZipEntry = new ZipArchiveEntry(project.getName() + ".xml");
            zipArchiveOuputStream.putArchiveEntry(sipXMLZipEntry);
            final InputStream is = new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8));
            try
            {
                ByteStreams.copy(is, zipArchiveOuputStream);
            }
            finally
            {
                zipArchiveOuputStream.closeArchiveEntry();
                is.close();
            }

            // Also put the SIP Inventory in place.
        }
        finally
        {
            zipArchiveOuputStream.close();
        }
    }

    private static void createDirForGroup(final Group group, final ZipArchiveOutputStream zipArchiveOuputStream, final String parentPath, final List<Group> selectedGroups, final SubmissionPackage submissionPackage) throws IOException
    {
        final String groupPath = parentPath + group.getName() + "/";
        final ZipArchiveEntry groupDirZipEntry = new ZipArchiveEntry(groupPath);
        zipArchiveOuputStream.putArchiveEntry(groupDirZipEntry);
        zipArchiveOuputStream.closeArchiveEntry();

        copyResearchFiles(group, zipArchiveOuputStream, groupPath, submissionPackage);

        for (final Group child : group.getGroups())
        {
            if (selectedGroups.contains(child))
            {
                createDirForGroup(child, zipArchiveOuputStream, groupPath, selectedGroups, submissionPackage);
            }
        }
    }

    private static void copyResearchFiles(final Group group, final ZipArchiveOutputStream zipArchiveOuputStream, final String parentPath, final SubmissionPackage submissionPackage) throws IOException
    {
        for (final ResearchFile researchFile : group.getResearchFiles())
        {
            if (submissionPackage.getResearchFiles().contains(researchFile))
            {
                final File theFile = researchFile.getFile();
                final ZipArchiveEntry zipEntry = new ZipArchiveEntry(parentPath + theFile.getName());
                zipEntry.setSize(theFile.length());
                zipArchiveOuputStream.putArchiveEntry(zipEntry);
                final InputStream is = new FileInputStream(theFile);
                try
                {
                    ByteStreams.copy(is, zipArchiveOuputStream);
                }
                finally
                {
                    zipArchiveOuputStream.closeArchiveEntry();
                    is.close();
                }
            }
        }
    }
}
