/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.startup;

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
import org.eclipse.core.runtime.Platform;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Inserts all of the Fields of Research on startup.
 */
public final class InsertFieldsOfResearch implements IStartup
{
    private static final Logger LOG = Logger.getLogger(InsertFieldsOfResearch.class);

    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * @{inheritDoc}
     */
    @Override
    public void earlyStartup()
    {
        final String workspaceDir = Platform.getInstallLocation().getURL().getPath();
        final File configurationDir = new File(workspaceDir, "configuration");
        final File initSqlFile = new File(configurationDir, "fieldsOfResearch.sql");

        final String sql;
        try
        {
            sql = Files.toString(initSqlFile, Charsets.UTF_8);
        }
        catch (final IOException e)
        {
            LOG.error("Could not read file " + initSqlFile.getAbsolutePath(), e);
            return;
        }
        final List<String> statements = Arrays.asList(sql.split(NEW_LINE));

        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final EntityManager em = emf.createEntityManager();
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
                for (final String statement : statements)
                {
                    try
                    {
                        stmt.execute(statement);
                    }
                    catch (final SQLIntegrityConstraintViolationException e)
                    {
                        // We already have the Field of Research we are trying to insert.
                    }
                }

                LOG.info("Completed inserting field of research codes");
                stmt.close();
            }
            else
            {
                LOG.info("Database contains " + statements.size() + " field of research codes. Insert skipped.");
            }
        }
        catch (final SQLException e)
        {
            LOG.error("Could not execute script " + initSqlFile.getAbsolutePath(), e);
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
}