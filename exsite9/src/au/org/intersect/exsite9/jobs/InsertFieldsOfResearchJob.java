/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.jobs;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Job used to insert the fields of research codes into the database.
 */
public final class InsertFieldsOfResearchJob extends Job
{
    private static final Logger LOG = Logger.getLogger(InsertFieldsOfResearchJob.class);
    private static final String NEW_LINE = System.getProperty("line.separator");

    private final EntityManagerFactory emf;
    private final File sqlFile;

    /**
     * Constructor
     */
    public InsertFieldsOfResearchJob()
    {
        super(InsertFieldsOfResearchJob.class.getName());
        this.emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final String workspaceDir = Platform.getInstallLocation().getURL().getPath();
        final File configurationDir = new File(workspaceDir, "configuration");
        this.sqlFile = new File(configurationDir, "fieldsOfResearch.sql");
    }

    /**
     * Constructor for use in unit tests only.
     * @param emf
     * @param sqlFile
     */
    InsertFieldsOfResearchJob(final EntityManagerFactory emf, final File sqlFile)
    {
        super(InsertFieldsOfResearchJob.class.getName());
        this.emf = emf;
        this.sqlFile = sqlFile;
    }


    @Override
    protected IStatus run(final IProgressMonitor monitor)
    {
        monitor.beginTask("Inserting Fields of Research", IProgressMonitor.UNKNOWN);
        try
        {
            final String sql;
            try
            {
                sql = Files.toString(this.sqlFile, Charsets.UTF_8);
            }
            catch (final IOException e)
            {
                LOG.error("Could not read file " + this.sqlFile.getAbsolutePath(), e);
                return Status.CANCEL_STATUS;
            }
            final List<String> statements = Arrays.asList(sql.split(NEW_LINE));

            final EntityManager em = this.emf.createEntityManager();
            final Connection connection = ((EntityManagerImpl)em.getDelegate()).getServerSession().getAccessor().getConnection();
            try
            {
                final Statement countStatement = connection.createStatement();
                final ResultSet rs = countStatement.executeQuery("SELECT COUNT(*) FROM FIELDOFRESEARCH");
                countStatement.close();
                rs.next();

                if (rs.getLong(1) != statements.size())
                {
                    LOG.info("Inserting field of research codes");

                    final Statement stmt = connection.createStatement();
                    int successInserts = 0;
                    int alreadyExists = 0;
                    for (final String statement : statements)
                    {
                        try
                        {
                            stmt.execute(statement);
                            successInserts++;
                        }
                        catch (final SQLIntegrityConstraintViolationException e)
                        {
                            alreadyExists++;
                            // We already have the Field of Research we are trying to insert.
                        }
                    }

                    LOG.info("Insertion of field of research codes complete. Attempted: " + statements.size() + " Successful: " + successInserts + " Skipped: " + alreadyExists);
                    stmt.close();
                }
                else
                {
                    LOG.info("Database contains " + statements.size() + " field of research codes. Insert skipped.");
                }
            }
            catch (final SQLException e)
            {
                LOG.error("Could not execute script " + this.sqlFile.getAbsolutePath(), e);
            }
            finally
            {
                try
                {
                    connection.close();
                }
                catch (final SQLException e)
                {
                    LOG.error("Could not close connection", e);
                }
                em.close();
            }
        }
        finally
        {
            monitor.done();
        }
        return Status.OK_STATUS;
    }

}
