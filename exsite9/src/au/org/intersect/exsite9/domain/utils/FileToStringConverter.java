/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.io.File;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

/**
 * Responsible for converting a {@link File} to a {@link String} for persistence to the database.
 */
public final class FileToStringConverter implements Converter
{
    private static final long serialVersionUID = 7746442903070005824L;

    /**
     * @{inheritDoc}
     */
    @Override
    public Object convertObjectValueToDataValue(final Object objectValue, final Session session)
    {
        if (!(objectValue instanceof File))
        {
            throw new IllegalArgumentException("Input must be a File");
        }
        final File file = (File) objectValue;
        return file.getAbsolutePath();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Object convertDataValueToObjectValue(final Object dataValue, final Session session)
    {
        if (!(dataValue instanceof String))
        {
            throw new IllegalArgumentException("Data value must be a String");
        }
        final String fullPath = (String) dataValue;
        final File file = new File(fullPath);
        return file;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isMutable()
    {
        return false;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void initialize(final DatabaseMapping mapping, final Session session)
    {
    }
}
