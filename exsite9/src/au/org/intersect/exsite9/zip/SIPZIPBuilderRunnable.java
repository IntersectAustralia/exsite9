package au.org.intersect.exsite9.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.xml.SIPXMLBuilder;

public final class SIPZIPBuilderRunnable implements IRunnableWithProgress
{
    private static final int BUF_SIZE = 0x1000; // 4K

    private static final Logger LOG = Logger.getLogger(SIPZIPBuilderRunnable.class);
    private static final String PROGRESS_MESSAGE = "Building ZIP file. This can take some time.";

    private final Project project;
    private final List<Group> selectedGroups;
    private final SubmissionPackage submissionPackage;
    private final File destinationFile;
    private IProgressMonitor progressMonitor;

    public SIPZIPBuilderRunnable(final Project project, final List<Group> selectedGroups, final SubmissionPackage submissionPackage, final File destinationFile)
    {
        this.project = project;
        this.selectedGroups = selectedGroups;
        this.submissionPackage = submissionPackage;
        this.destinationFile = destinationFile;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
        this.progressMonitor = monitor;

        // Determine the size of all files we need to compress - so we can provide progress.
        long totalSizeBytes = 0;
        for (final ResearchFile researchFile : this.submissionPackage.getResearchFiles())
        {
            totalSizeBytes += researchFile.getFile().length();
        }
        final int totalSizeKiloBytes = (int) (totalSizeBytes / 1024);
        this.progressMonitor.beginTask(PROGRESS_MESSAGE, totalSizeKiloBytes);

        try
        {
            LOG.info("Writing ZIP file " + destinationFile.getAbsolutePath());

            final ZipArchiveOutputStream zipStream = new ZipArchiveOutputStream(destinationFile);
            zipStream.setUseZip64(Zip64Mode.AsNeeded);
            final WritableByteChannel zipByteChannel = Channels.newChannel(zipStream);

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
                final String inventoryInput = SIPZIPInventoryFileBuilder.buildInventoryFile(project, submissionPackage);
                final ZipArchiveEntry inventoryFileZipEntry = new ZipArchiveEntry(submissionPackage.getName() + "-Inventory.txt");
                zipStream.putArchiveEntry(inventoryFileZipEntry);
                final ReadableByteChannel inventoryChannel = Channels.newChannel(new ByteArrayInputStream(inventoryInput.getBytes(Charsets.UTF_8)));

                try
                {
                    ByteStreams.copy(inventoryChannel, zipByteChannel);
                }
                finally
                {
                    zipStream.closeArchiveEntry();
                    inventoryChannel.close();
                }
            }
            catch (final InterruptedException e)
            {
                // User has requested the operation to cancel. Delete the partially built file.
                zipStream.close();
                this.destinationFile.delete();
                throw e;
            }
            finally
            {
                zipStream.close();
            }
            LOG.info("Completed writing ZIP file " + destinationFile.getAbsolutePath());
        }
        catch (final IOException e)
        {
            LOG.error("Could not create ZIP file " + destinationFile.getAbsolutePath(), e);
            throw new InvocationTargetException(e);
        }
        finally
        {
            this.progressMonitor.done();
        }
    }

    private void createDirForGroup(final Group group, final ZipArchiveOutputStream zipStream, final WritableByteChannel zipByteChannel, final String parentPath, final List<Group> selectedGroups, final SubmissionPackage submissionPackage) throws IOException, InterruptedException
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

    private void copyResearchFiles(final Group group, final ZipArchiveOutputStream zipStream, final WritableByteChannel zipByteChannel, final String parentPath, final SubmissionPackage submissionPackage) throws IOException, InterruptedException
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
                    copy(fileByteChannel, zipByteChannel);
                }
                finally
                {
                    zipStream.closeArchiveEntry();
                    fileByteChannel.close();
                }
            }
        }
    }

    /**
     * Copied from Google Guava {@link ByteStreams#copy(ReadableByteChannel, WritableByteChannel)} and modified to provide progress feedback and the ability to cancel
     * mid-way through a copy.
     *
     * Copies all bytes from the readable channel to the writable channel.
     * Does not close or flush either channel.
     * @param from the readable channel to read from
     * @param to the writable channel to write to
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    private long copy(final ReadableByteChannel from, final WritableByteChannel to) throws IOException, InterruptedException
    {
        final ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);
        long total = 0;
        while (from.read(buf) != -1)
        {
            buf.flip();
            while (buf.hasRemaining())
            {
                final int bytesRead = to.write(buf);
                total += bytesRead;
                this.progressMonitor.worked(bytesRead / 1024);
                if (this.progressMonitor.isCanceled())
                {
                    throw new InterruptedException("User Cancelled");
                }
            }
            buf.clear();
        }
        return total;
    }
}
