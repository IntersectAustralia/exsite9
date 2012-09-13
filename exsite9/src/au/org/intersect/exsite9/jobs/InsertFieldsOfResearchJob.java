/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
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

/**
 * Job used to insert the fields of research codes into the database.
 */
public final class InsertFieldsOfResearchJob extends Job
{
    private static final Logger LOG = Logger.getLogger(InsertFieldsOfResearchJob.class);
    public static final String JOB_NAME = "Loading Fields of Research";

    private final EntityManagerFactory emf;
    private final File sqlFile;

    /**
     * Constructor
     */
    public InsertFieldsOfResearchJob()
    {
        super(JOB_NAME);
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
        super(JOB_NAME);
        this.emf = emf;
        this.sqlFile = sqlFile;
    }


    @Override
    protected IStatus run(final IProgressMonitor monitor)
    {
        monitor.beginTask("Inserting Fields of Research", IProgressMonitor.UNKNOWN);
        try
        {
            final List<String> statements = new ArrayList<String>();
            try
            {
                final BufferedReader br = new BufferedReader(new FileReader(this.sqlFile));
                String line;
                while ((line = br.readLine()) != null)
                {
                    statements.add(line);
                }
            }
            catch (final IOException e)
            {
                LOG.error("Could not read file " + this.sqlFile.getAbsolutePath(), e);
                return Status.CANCEL_STATUS;
            }

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
