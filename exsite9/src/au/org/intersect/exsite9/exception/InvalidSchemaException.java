/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.exception;

/**
 * An Exception to indicate that a schema is invalid.
 */
public final class InvalidSchemaException extends Exception
{
    private static final long serialVersionUID = 8734323968396851378L;

    public InvalidSchemaException(final String reason)
    {
        super(reason);
    }
}
