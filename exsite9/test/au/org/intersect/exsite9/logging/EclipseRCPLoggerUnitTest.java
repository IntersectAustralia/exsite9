/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.logging;

import static org.mockito.Mockito.*;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import au.org.intersect.exsite9.Application;

/**
 * Tests {@link EclipseRCPLogger}
 */
public final class EclipseRCPLoggerUnitTest
{

    @Test
    public void testEclipseRCPLogger()
    {
        final StatusManager statusManager = mock(StatusManager.class);
        final EclipseRCPLogger toTest = new EclipseRCPLogger(statusManager);

        final String infoMessage = "some info message";
        toTest.info(infoMessage);

        verify(statusManager).handle(argThat(new BaseMatcher<Status>()
        {
            @Override
            public boolean matches(final Object item)
            {
                final Status status = (Status) item;
                return status.getCode() == IStatus.INFO && status.getMessage().equals(infoMessage) &&
                    status.getSeverity() == IStatus.INFO && status.getPlugin().equals(Application.class.getPackage().getName()) &&
                    status.getException() == null;
            }

            @Override
            public void describeTo(Description description)
            {
            }
        }));

        final String warningMessage = "this is a warning. Don't be alarmed";
        toTest.warning(warningMessage);
        verify(statusManager).handle(argThat(new BaseMatcher<Status>()
        {
            @Override
            public boolean matches(final Object item)
            {
                final Status status = (Status) item;
                return status.getCode() == IStatus.WARNING && status.getMessage().equals(warningMessage) &&
                    status.getSeverity() == IStatus.WARNING && status.getPlugin().equals(Application.class.getPackage().getName()) &&
                    status.getException() == null;
            }

            @Override
            public void describeTo(Description description)
            {
            }
        }));

        final Throwable warningException = new NullPointerException("some fake null pointer");
        toTest.warning(warningMessage, warningException);
        verify(statusManager).handle(argThat(new BaseMatcher<Status>()
        {
            @Override
            public boolean matches(final Object item)
            {
                final Status status = (Status) item;
                return status.getCode() == IStatus.WARNING && status.getMessage().equals(warningMessage) &&
                    status.getSeverity() == IStatus.WARNING && status.getPlugin().equals(Application.class.getPackage().getName()) &&
                    status.getException() == warningException;
            }

            @Override
            public void describeTo(Description description)
            {
            }
        }));

        final String errorMessage = "this is an error!";
        toTest.error(errorMessage);
        verify(statusManager).handle(argThat(new BaseMatcher<Status>()
        {
            @Override
            public boolean matches(final Object item)
            {
                final Status status = (Status) item;
                return status.getCode() == IStatus.ERROR && status.getMessage().equals(errorMessage) &&
                    status.getSeverity() == IStatus.ERROR && status.getPlugin().equals(Application.class.getPackage().getName()) &&
                    status.getException() == null;
            }

            @Override
            public void describeTo(Description description)
            {
            }
        }));

        final Throwable errorException = new RuntimeException("some runtime exception");
        toTest.error(errorMessage, errorException);
        verify(statusManager).handle(argThat(new BaseMatcher<Status>()
        {
            @Override
            public boolean matches(final Object item)
            {
                final Status status = (Status) item;
                return status.getCode() == IStatus.ERROR && status.getMessage().equals(errorMessage) &&
                    status.getSeverity() == IStatus.ERROR && status.getPlugin().equals(Application.class.getPackage().getName()) &&
                    status.getException() == errorException;
            }

            @Override
            public void describeTo(Description description)
            {
            }
        }));
    }
}