/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.exception;

/**
 * An Exception to indicate that a there is not enough disk space to write the zip file.
 */
public final class NotEnoughSpaceForZIPException extends Exception
{
    
    private static final long serialVersionUID = -4474422037107912245L;

    public NotEnoughSpaceForZIPException(final String reason)
    {
        super(reason);
    }
}
