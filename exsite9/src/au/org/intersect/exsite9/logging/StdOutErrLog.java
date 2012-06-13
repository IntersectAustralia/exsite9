/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.logging;

import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 * Used to capture stderr and stdout to Log4j
 */
public final class StdOutErrLog
{
    private static final Logger LOG = Logger.getLogger(StdOutErrLog.class);

    /**
     * Redirects stdout and stderr to log4j.
     */
    public static void redirect()
    {
        System.setOut(createInfoLoggingProxy(System.out));
        System.setErr(createErrorLoggingProxy(System.err));
    }

    private static PrintStream createInfoLoggingProxy(final PrintStream realPrintStream)
    {
        return new PrintStream(realPrintStream)
        {
            @Override
            public void print(final String string)
            {
                LOG.info(string);
            }
        };
    }

    private static PrintStream createErrorLoggingProxy(final PrintStream realPrintStream)
    {
        return new PrintStream(realPrintStream)
        {
            @Override
            public void print(final String string)
            {
                LOG.error(string);
            }
        };
    }
}
