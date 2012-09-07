/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.startup;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.FieldOfResearchDAO;
import au.org.intersect.exsite9.domain.FieldOfResearch;

/**
 * Tests {@link InsertFieldsOfResearch}
 */
public final class InsertFieldsOfResearchUnitTest extends DAOTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");

    @Test
    public void test() throws IOException
    {
        final File sqlFile = File.createTempFile("sqlFile", "sql");
        sqlFile.deleteOnExit();

        final String forSQL = "INSERT INTO FIELDOFRESEARCH(code,name) VALUES('010000','Mathematical Sciences')" + NEW_LINE +
                              "INSERT INTO FIELDOFRESEARCH(code,name) VALUES('010100','Pure Mathematics')";
        Files.write(forSQL, sqlFile, Charsets.UTF_8);

        final InsertFieldsOfResearch toTest = new InsertFieldsOfResearch(super.emf, sqlFile);
        toTest.earlyStartup();

        final FieldOfResearchDAO forDAO = new FieldOfResearchDAO(createEntityManager());
        final List<FieldOfResearch> inserted = forDAO.getAll();
        assertEquals(2, inserted.size());

        final FieldOfResearch for1 = inserted.get(0);
        final FieldOfResearch for2 = inserted.get(1);

        assertEquals("010000", for1.getCode());
        assertEquals("Mathematical Sciences", for1.getName());
        assertEquals("010100", for2.getCode());
        assertEquals("Pure Mathematics", for2.getName());
    }
}
