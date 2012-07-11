package au.org.intersect.exsite9.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.util.ProgressRunnableWithError;
import au.org.intersect.exsite9.xml.SIPXMLBuilder;

public final class SIPZIPBuilder extends ProgressRunnableWithError
{
    private static final Logger LOG = Logger.getLogger(SIPZIPBuilder.class);

    private final Project project;
    private final List<Group> selectedGroups;
    private final SubmissionPackage submissionPackage;
    private final File destinationFile;
    private Throwable error;

    public SIPZIPBuilder(final Project project, final List<Group> selectedGroups, final SubmissionPackage submissionPackage, final File destinationFile)
    {
        super("Building ZIP file. This can take some time.");
        this.project = project;
        this.selectedGroups = selectedGroups;
        this.submissionPackage = submissionPackage;
        this.destinationFile = destinationFile;
    }

    @Override
    public void doRun() throws IOException
    {
        final ZipArchiveOutputStream zipStream = new ZipArchiveOutputStream(destinationFile);
        zipStream.setUseZip64(Zip64Mode.AsNeeded);
        final WritableByteChannel zipByteChannel = Channels.newChannel(zipStream);

        LOG.info("Writing ZIP file " + destinationFile.getAbsolutePath());

        try
        {
            for (final Group group : project.getRootNode().getGroups())
            {
                if (selectedGroups.contains(group))
                {
                    createDirForGroup(group, zipStream, zipByteChannel, "", selectedGroups, submissionPackage);
                }
            }
            copyResearchFiles(project.getRootNode(), zipStream, zipByteChannel, "", submissionPackage);

            // Put the SIP XML in place.
            final String xml = SIPXMLBuilder.buildXML(project, selectedGroups, submissionPackage, true);
            
            final ZipArchiveEntry sipXMLZipEntry = new ZipArchiveEntry(project.getName() + ".xml");
            zipStream.putArchiveEntry(sipXMLZipEntry);

            final ReadableByteChannel sipXmlByteChannel = Channels.newChannel(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
            try
            {
                ByteStreams.copy(sipXmlByteChannel, zipByteChannel);
            }
            finally
            {
                zipStream.closeArchiveEntry();
                sipXmlByteChannel.close();
            }

            // Also put the SIP Inventory in place.
        }
        finally
        {
            zipStream.close();
        }
        LOG.info("Completed writing ZIP file " + destinationFile.getAbsolutePath());
    }

    /**
     * Obtains the error. {@code null} if there was no error.
     * @return The error.
     */
    public Throwable getError()
    {
        return this.error;
    }

    private static void createDirForGroup(final Group group, final ZipArchiveOutputStream zipStream, final WritableByteChannel zipByteChannel, final String parentPath, final List<Group> selectedGroups, final SubmissionPackage submissionPackage) throws IOException
    {
        final String groupPath = parentPath + group.getName() + "/";
        final ZipArchiveEntry groupDirZipEntry = new ZipArchiveEntry(groupPath);
        zipStream.putArchiveEntry(groupDirZipEntry);
        zipStream.closeArchiveEntry();

        copyResearchFiles(group, zipStream, zipByteChannel, groupPath, submissionPackage);

        for (final Group child : group.getGroups())
        {
            if (selectedGroups.contains(child))
            {
                createDirForGroup(child, zipStream, zipByteChannel, groupPath, selectedGroups, submissionPackage);
            }
        }
    }

    private static void copyResearchFiles(final Group group, final ZipArchiveOutputStream zipStream, final WritableByteChannel zipByteChannel, final String parentPath, final SubmissionPackage submissionPackage) throws IOException
    {
        for (final ResearchFile researchFile : group.getResearchFiles())
        {
            if (submissionPackage.getResearchFiles().contains(researchFile))
            {
                final File theFile = researchFile.getFile();
                
                final ZipArchiveEntry zipEntry = new ZipArchiveEntry(parentPath + theFile.getName());
                zipEntry.setSize(theFile.length());
                zipStream.putArchiveEntry(zipEntry);

                final ReadableByteChannel fileByteChannel = Channels.newChannel(new FileInputStream(theFile));
                try
                {
                    ByteStreams.copy(fileByteChannel, zipByteChannel);
                }
                finally
                {
                    zipStream.closeArchiveEntry();
                    fileByteChannel.close();
                }
            }
        }
    }
}
