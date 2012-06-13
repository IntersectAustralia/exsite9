package org.eclipse.persistence.logging;

import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.EclipseLinkLogRecord;
import org.eclipse.persistence.logging.JavaLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.eclipse.persistence.sessions.Session;

/**
 * PUBLIC: 
 * This is a wrapper class for org.apache.commons.logging.Log.
 * It is used when messages need to be logged through apache commons logging 1.1.
 *
 * History :
 * 
 * - 05/05/2009 : Updated API to EclipseLink 1.1 version + Javadoc fixes
 * - 08/05/2009 : Fix for log4j levels in conflict with eclipselink jpa levels (shouldLog)
 * 
 * TODO : Use Enum instead of int values for Levels (OO Design)
 *
 * Requires :
 * - Jakarta Commons Logging 1.1 : http://commons.apache.org/logging/
 *     - commons-logging-1.1.1.jar
 *
 * - Log4j 1.2 : http://logging.apache.org/log4j/
 *     - log4j-1.2.15.jar
 *
 *
 * @author laurent bourges (voparis) : bourges.laurent@gmail.com
 *
 * @see AbstractSessionLog
 * @see JavaLog
 * @see SessionLogEntry
 */
public final class CommonsLoggingSessionLog extends AbstractSessionLog {
    //~ Constants --------------------------------------------------------------------------------------------------------

    /** 
     * internal debugger FLAG : use System.out
     */
    public static final boolean FORCE_INTERNAL_DEBUG = false;
    /** 
     * internal debugger FLAG : use a stack trace to find caller class
     */
    public static final boolean FORCE_INTERNAL_DEBUG_STACK = false;
    /** 
     * internal cache FLAG for LogWrapper's levels
     */
    public static final boolean USE_INTERNAL_CACHE = true;
    /** 
     * internal apache commons Logging diagnostic FLAG : use System.out
     */
    public static final boolean FORCE_APACHE_COMMONS_LOGGING_DIAGNOSTICS = false;
    /** 
     * value -1 corresponds to an undefined Level
     */
    public static final int UNDEFINED_LEVEL = -1;
    /**
     * Stores the default session name in case there is the session name is missing. org.eclipse.persistence
     * package used by Log4J configuration
     */
    public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence";
    /** 
     * org.eclipse.persistence.default used by Log4J configuration
     */
    public static final String DEFAULT_ECLIPSELINK_NAMESPACE = ECLIPSELINK_NAMESPACE + ".default";
    /** 
     * org.eclipse.persistence.session used by Log4J configuration
     */
    public static final String SESSION_ECLIPSELINK_NAMESPACE = ECLIPSELINK_NAMESPACE + ".session";
    /** 
     * Copied from JavaLog for compatibility issues
     */
    public static final String LOGGING_LOCALIZATION_STRING = "org.eclipse.persistence.internal.localization.i18n.LoggingLocalizationResource";
    /** 
     * Copied from JavaLog for compatibility issues
     */
    public static final String TRACE_LOCALIZATION_STRING = "org.eclipse.persistence.internal.localization.i18n.TraceLocalizationResource";
    /** 
     * Stores all the java.util.logging.Levels.  The indexes are EclipseLink logging levels.
     */
    public static final Level[] JAVA_LEVELS = new Level[]{
        Level.ALL, Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG, Level.INFO,
        Level.WARNING, Level.SEVERE, Level.OFF
    };


    static {
        /* static Initializer to call onInit method */
        onInit();
    }

    //~ Members ----------------------------------------------------------------------------------------------------------
    /**
     * formats the EclipseLinkLogRecords. Acts as a static variable but not declared static to avoid classLoader
     * leaks  (clone does not deep clone this instance)
     */
    private final FastLogFormatter LOG_FORMATTER = new FastLogFormatter();
    /**
     * Represents the HashMap that stores all the name space strings. The keys are category names.  The values are
     * namespace strings. Acts as a static variable but not declared static to avoid classLoader leaks (clone does not
     * deep clone this map). Note : Unsynchronized Map = should be thread-safe !
     */
    private final Map<String, String> NAMESPACE_MAP = new HashMap<String, String>(32);
    /**
     * LogWrapper instances. Acts as a static variable but not declared static to avoid classLoader leaks (clone
     * does not deep clone this map) Note : Unsynchronized Map = should be thread-safe !
     */
    private final Map<String, LogWrapper> CATEGORY_LOGGERS = new HashMap<String, LogWrapper>(32);
    /** 
     * Stores the namespace for session, i.e."org.eclipse.persistence.session.#sessionname#".
     */
    private String sessionNameSpace;

    //~ Constructors -----------------------------------------------------------------------------------------------------
    /**
     * PUBLIC:
     *
     * CommonsLoggingSessionLog Constructor.
     * 
     * Used by :
     * org.eclipse.persistence.internal.jpa.EntityManagerSetupImpl.updateLoggers :
     * creates an instance for singletonLog and sessionLog
     *
     * This adds a root logger for DEFAULT_ECLIPSELINK_NAMESPACE.
     */
    public CommonsLoggingSessionLog() {
        super();

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.new : instance : " + this, true);
        }

        addLogger(DEFAULT_ECLIPSELINK_NAMESPACE, DEFAULT_ECLIPSELINK_NAMESPACE);
    }

    //~ Methods ----------------------------------------------------------------------------------------------------------
    /**
     * PUBLIC:
     *
     * OnInit method : define system properties for org.apache.commons.logging
     */
    public static final void onInit() {

        if (FORCE_APACHE_COMMONS_LOGGING_DIAGNOSTICS) {
            /**
             * The name (<code>org.apache.commons.logging.diagnostics.dest</code>)
             * of the property used to enable internal commons-logging
             * diagnostic output, in order to get information on what logging
             * implementations are being discovered, what classloaders they
             * are loaded through, etc.
             * <p>
             * If a system property of this name is set then the value is
             * assumed to be the name of a file. The special strings
             * STDOUT or STDERR (case-sensitive) indicate output to
             * System.out and System.err respectively.
             * <p>
             * Diagnostic logging should be used only to debug problematic
             * configurations and should not be set in normal production use.
             */
            System.setProperty("org.apache.commons.logging.diagnostics.dest", "STDOUT");
        }
    }

    /**
     * PUBLIC:
     *
     * onExit method : release all ClassLoader references due to apache commons logging LogFactory
     *
     * NOTE : <b>This method must be called in the context of a web application via ServletContextListener.contextDestroyed(ServletContextEvent)</b>
     *
     * @see org.apache.commons.logging.LogFactory#release(ClassLoader)
     */
    public static final void onExit() {
        // Classloader unload problem with commons-logging :
        LogFactory.release(Thread.currentThread().getContextClassLoader());
    }

    /**
     * PUBLIC:  Return the effective log level for the name space extracted from session and category. If a
     * Logger's level is set to be null then the Logger will use an effective Level that will be obtained by walking up
     * the parent tree and using the first non-null Level.
     *
     * @param category category
     *
     * @return the effective level according to the java.util.logging.Levels
     */
    @Override
    public final int getLevel(final String category) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLevel : IN : category : " + category);
        }

        final LogWrapper lw = getLogWrapper(category);

        int l = OFF;

        if (lw != null) {
            l = lw.getLevel();
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLevel : OUT : category : " + category + " : level : " + getLevelString(l));
        }

        return l;
    }

    /**
     * PUBLIC:  Set the log level to a logger with name space extracted from the given category.
     *
     * @param level value according to the java.util.logging.Levels
     * @param category category
     */
    @Override
    public final void setLevel(final int level, final String category) {
        AccessController.doPrivileged(
                new PrivilegedAction<Object>() {

                    public Object run() {
                        if (FORCE_INTERNAL_DEBUG) {
                            debug("CommonsLoggingSessionLog.setLevel : IN : category : " + category + " to level : " +
                                    getLevelString(level), true);
                        }

                        final LogWrapper lw = getLogWrapper(category);

                        if (lw == null) {
                            error("CommonsLoggingSessionLog.setLevel : category not found : " + category);
                        } else {
                            final Logger logger = getLog4JLogger(lw.getLog());

                            if (logger == null) {
                                error("CommonsLoggingSessionLog.setLevel : Logger not found : " + category + " : " + lw.getLog());
                            } else {

                                final org.apache.log4j.Level realLevel = getLevelFor(level);

                                if (realLevel == org.apache.log4j.Level.OFF) {
                                    // force level to OFF :
                                    lw.setLevel(OFF);
                                    error("CommonsLoggingSessionLog.setLevel : unknown level : " + getLevelString(level) + " : level set to OFF");
                                } else {
                                    lw.setLevel(level);
                                }
                                logger.setLevel(realLevel);

                                if (logger.isEnabledFor(org.apache.log4j.Level.WARN)) {
                                    logger.warn("CommonsLoggingSessionLog.setLevel : Logger Level set to : " + logger.getLevel());
                                }

                                if (FORCE_INTERNAL_DEBUG) {
                                    debug("CommonsLoggingSessionLog.setLevel : OUT : category : " + category + " to level : " +
                                            logger.getLevel());
                                }
                            }
                        }

                        // nothing to return :
                        return null;
                    }
                });
    }

    /**
     * PUBLIC:  Set the output stream that will receive the formatted log entries. DO nothing as Log4J manages the
     * appenders (console or files) via Log4J configuration
     *
     * @param fileOutputStream the file output stream will receive the formatted log entries.
     */
    @Override
    public final void setWriter(final OutputStream fileOutputStream) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.setWriter : stream : " + fileOutputStream);
        }

    // do nothing
    }

    /**
     * PUBLIC: Set the session and session namespace.
     *
     * @param pSession an eclipselink Session
     */
    @Override
    public final void setSession(final Session pSession) {
        super.setSession(pSession);

        if (pSession != null) {
            final String sessionName = pSession.getName();

            if ((sessionName != null) && (sessionName.length() != 0)) {
                this.sessionNameSpace = SESSION_ECLIPSELINK_NAMESPACE + "." + sessionName;
            } else {
                this.sessionNameSpace = DEFAULT_ECLIPSELINK_NAMESPACE;
            }

            if (FORCE_INTERNAL_DEBUG) {
                debug("CommonsLoggingSessionLog.setSession : sessionNameSpace : " + this.sessionNameSpace);
            }

            //Initialize loggers eagerly
            addLogger(this.sessionNameSpace, this.sessionNameSpace);

            addDefaultLoggers(this.sessionNameSpace);
        }
    }

    /**
     * PUBLIC: Check if a message of the given lev would actually be logged by the logger with name space built
     * from the given session and category. Return the shouldLog for the given category Note : this method is very very
     * used so optimized for performance.
     *
     * @param level value according to the java.util.logging.Levels
     * @param category category
     *
     * @return true if the given message will be logged
     */
    @Override
    public final boolean shouldLog(final int level, final String category) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.shouldLog : IN : category : " + category + " : " + getLevelString(level));
        }

        boolean res = false;

        switch (level) {
            case OFF:
                res = false;

                break;

            case ALL:
                res = true;

                break;

            default:

                final LogWrapper lw = getLogWrapper(category);

                if (lw == null) {
                    error("CommonsLoggingSessionLog.shouldLog : category : " + category + " - NO LOGGER FOUND");
                    res = false;
                } else {
                    res = level >= lw.getLevel();
                }
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.shouldLog : OUT : category : " + category + " : " + res);
        }

        return res;
    }

    /**
     * PUBLIC: Log a SessionLogEntry
     *
     * @param entry SessionLogEntry that holds all the information for a EclipseLink logging event
     */
    public final void log(final SessionLogEntry entry) {
        if (shouldLog(entry.getLevel(), entry.getNameSpace())) {
            if (FORCE_INTERNAL_DEBUG) {
                debug("CommonsLoggingSessionLog.log : namespace : " + entry.getNameSpace() + " message : " + entry.getMessage());
            }

            final Log log = getLog(entry.getNameSpace());

            if (log == null) {
                error("CommonsLoggingSessionLog.log : no Log found for : " + entry.getNameSpace());
            } else {
                final Level javaLevel = getJavaLevel(entry.getLevel());

                internalLog(entry, javaLevel, log);
            }
        }
    }

    /**
     * PUBLIC: Log a throwable.
     *
     * @param throwable a throwable
     */
    @Override
    public final void throwing(final Throwable throwable) {
        final Log log = getLog(null);

        if (log != null) {
            log.error(null, throwable);
        }
    }

    /**
     * INTERNAL: Each session owns its own session log because session is stored in the session log
     *
     * @return value TODO : Value Description
     */
    @Override
    public final Object clone() {
        // There is no special treatment required for cloning here
        // The state of this object is described  by member variables sessionLogger and categoryLoggers.
        // This state depends on session.
        // If session for the clone is going to be the same as session for this there is no
        // need to do "deep" cloning.
        // If not, the session being cloned should call setSession() on its JavaLog object to initialize it correctly.
        final CommonsLoggingSessionLog cloneLog = (CommonsLoggingSessionLog) super.clone();

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.clone : cloned instance : " + cloneLog, true);
        }

        return cloneLog;
    }

    /**
     * INTERNAL: Add Logger to the categoryloggers.
     *
     * @param loggerCategory category
     * @param loggerNameSpace name space
     */
    private final void addLogger(final String loggerCategory, final String loggerNameSpace) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.addLogger : category : " + loggerCategory + " in name space : " + loggerNameSpace);
        }

        this.CATEGORY_LOGGERS.put(loggerCategory, new LogWrapper(this, loggerCategory, LogFactory.getLog(loggerNameSpace)));
    }

    /**
     * INTERNAL: Return the name space for the given category from the map.
     *
     * @param category category
     *
     * @return name space for the given category
     */
    private final String getNameSpaceString(final String category) {
        if (getSession() == null) {
            return DEFAULT_ECLIPSELINK_NAMESPACE;
        } else if ((category == null) || (category.length() == 0)) {
            return this.sessionNameSpace;
        } else {
            return this.NAMESPACE_MAP.get(category);
        }
    }

    /**
     * INTERNAL: Return the LogWrapper instance for the given category Note : this method is very very used so
     * optimized for performance.
     *
     * @param category category
     *
     * @return LogWrapper instance or null if not found
     */
    private final LogWrapper getLogWrapper(final String category) {
        LogWrapper lw = null;

        if (getSession() == null) {
            if (FORCE_INTERNAL_DEBUG) {
                debug("CommonsLoggingSessionLog.getLogWrapper : " + category + " : SESSION NULL");
            }

            lw = this.CATEGORY_LOGGERS.get(DEFAULT_ECLIPSELINK_NAMESPACE);
        } else {
            lw = this.CATEGORY_LOGGERS.get(category);

            if (lw == null) {
                if (FORCE_INTERNAL_DEBUG) {
                    debug("CommonsLoggingSessionLog.getLogWrapper : " + category + " : CATEGORY IS NULL ?");
                }

                // really few cases :
                if ((category == null) || (category.length() == 0)) {
                    lw = this.CATEGORY_LOGGERS.get(this.sessionNameSpace);
                }
            }
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLogWrapper : " + category + " = " + lw);
        }

        return lw;
    }

    /**
     * INTERNAL: Return the apache commons logging Log instance for the given category
     *
     * @param category category
     *
     * @return value Log instance or null if not found
     */
    private final Log getLog(final String category) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLogger : IN : category : " + category);
        }

        final LogWrapper lw = getLogWrapper(category);

        Log log = null;

        if (lw != null) {
            log = lw.getLog();
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLogger : OUT : log : " + log);
        }

        return log;
    }

    /**
     * INTERNAL: Adds default loggers for the given name space
     *
     * @param namespace name space
     */
    private final void addDefaultLoggers(final String namespace) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.addDefaultLoggers : nameSpace : " + namespace);
        }

        String loggerCategory;
        String loggerNameSpace;

        final String[] categories = SessionLog.loggerCatagories;

        final int size = categories.length;

        for (int i = 0; i < size; i++) {
            loggerCategory = categories[i];
            loggerNameSpace = namespace + "." + loggerCategory;

            this.NAMESPACE_MAP.put(loggerCategory, loggerNameSpace);
            addLogger(loggerCategory, loggerNameSpace);
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.addDefaultLoggers : NAMESPACE_MAP : " + this.NAMESPACE_MAP);
            debug("CommonsLoggingSessionLog.addDefaultLoggers : CATEGORY_LOGGERS : " + this.CATEGORY_LOGGERS);
        }
    }

    /**
     * INTERNAL:  Build a LogRecord
     *
     * @param entry SessionLogEntry that holds all the information for a EclipseLink logging event
     * @param level according to eclipselink level
     * @param log commons-logging 1.1 wrapper
     */
    private final void internalLog(final SessionLogEntry entry, final Level level, final Log log) {
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.internalLog : " + computeMessage(entry, level));
        }

        final int entryLevel = entry.getLevel();

        switch (entryLevel) {
            case SEVERE:
                log.error(computeMessage(entry, level));

                break;

            case WARNING:
                log.warn(computeMessage(entry, level));

                break;

            case INFO:
            case CONFIG:
                log.info(computeMessage(entry, level));

                break;

            case FINE:
            case FINER:
            case FINEST:
                log.debug(computeMessage(entry, level));

                break;

            case ALL:
                log.trace(computeMessage(entry, level));

                break;

            case OFF:
                break;

            default:
                error("CommonsLoggingSessionLog.internalLog : unknown level : " + entryLevel);

                break;
        }
    }

    /**
     * INTERNAL: Computes the log message
     *
     * @param entry SessionLogEntry that holds all the information for a EclipseLink logging event
     * @param level according to eclipselink level
     *
     * @return value TODO : Value Description
     */
    private final String computeMessage(final SessionLogEntry entry, final Level level) {
        // Format message so that we do not depend on the bundle
        final EclipseLinkLogRecord lr = new EclipseLinkLogRecord(level, formatMessage(entry));

        lr.setSourceClassName(null);
        lr.setSourceMethodName(null);
        lr.setLoggerName(getNameSpaceString(entry.getNameSpace()));

        if (shouldPrintSession()) {
            lr.setSessionString(getSessionString(entry.getSession()));
        }

        if (shouldPrintConnection()) {
            lr.setConnection(entry.getConnection());
        }

        lr.setThrown(entry.getException());
        lr.setShouldLogExceptionStackTrace(shouldLogExceptionStackTrace());

        lr.setShouldPrintDate(shouldPrintDate());
        lr.setShouldPrintThread(shouldPrintThread());

        return this.LOG_FORMATTER.format(lr);
    }

    /**
     * INTERNAL: Return the corresponding java.util.logging.Level for a given eclipselink level.
     *
     * @param level according to eclipselink level
     *
     * @return value according to the java.util.logging.Levels
     */
    private static final Level getJavaLevel(final int level) {
        return JAVA_LEVELS[level];
    }

    /**
     * INTERNAL: Returns Log4JLogger instance
     *
     * @param log commons-logging 1.1 wrapper
     *
     * @return Log4JLogger instance
     */
    private static final Logger getLog4JLogger(final Log log) {
        Logger l = null;

        if (log instanceof Log4JLogger) {
            l = ((Log4JLogger) log).getLogger();
        }

        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLog4JLogger : " + l);
        }

        return l;
    }

    /**
     * PUBLIC: SHOULD BE in AbstractSessionLog
     *
     * @see AbstractSessionLog#getLevelString() Return the log level as a string value.
     */
    private static final String getLevelString(final int level) {
        switch (level) {
            case OFF:
                return "OFF";

            case SEVERE:
                return "SEVERE";

            case WARNING:
                return "WARNING";

            case INFO:
                return "INFO";

            case CONFIG:
                return "CONFIG";

            case FINE:
                return "FINE";

            case FINER:
                return "FINER";

            case FINEST:
                return "FINEST";

            case ALL:
                return "ALL";

            default:
                return "INFO";
        }
    }

    /**
     * Returns the real Log4J Level 
     * @param level eclipselink level
     * @return org.apache.log4j.Level
     */
    private static final org.apache.log4j.Level getLevelFor(final int level) {
        org.apache.log4j.Level realLevel = null;
        switch (level) {
            case SEVERE:
                realLevel = org.apache.log4j.Level.ERROR;

                break;

            case WARNING:
                realLevel = org.apache.log4j.Level.WARN;

                break;

            case INFO:
            case CONFIG:
                realLevel = org.apache.log4j.Level.INFO;

                break;

            case FINE:
            case FINER:
            case FINEST:
                realLevel = org.apache.log4j.Level.DEBUG;

                break;

            case ALL:
                realLevel = org.apache.log4j.Level.ALL;

                break;

            case OFF:
                realLevel = org.apache.log4j.Level.OFF;

                break;

            default:
                realLevel = org.apache.log4j.Level.OFF;
                error("CommonsLoggingSessionLog.getLevelFor : unknown level : " + getLevelString(level) + " = OFF");

                break;
        }
        if (FORCE_INTERNAL_DEBUG) {
            debug("CommonsLoggingSessionLog.getLevelFor : level : " + getLevelString(level) + " = " + realLevel);
        }
        return realLevel;

    }

    /**
     * Prints the message in Std out
     * @param message message to print
     */
    protected final static void debug(final String message) {
        debug(message, false);
    }

    /**
     * Prints the message in Std out
     * @param message message to print
     * @param printStack adds a stack trace to find caller class
     */
    protected final static void debug(final String message, final boolean printStack) {
        System.out.println(message);
//        if (printStack && FORCE_INTERNAL_DEBUG_STACK) {
//            // to inspect the calling stack :
//            new Throwable().printStackTrace(System.out);
//        }
    }

    /**
     * Prints the message in Std err
     * @param message message to print
     */
    protected final static void error(final String message) {
        System.err.println(message);
    }

    /**
     * Prints the message in Std err
     * @param message message to print
     */
    protected final static void error(final Throwable th) {
        th.printStackTrace(System.err);
    }

//~ Inner Classes ----------------------------------------------------------------------------------------------------
    /**
     * INTERNAL: LogWrapper class wraps the real apache commons logging Log instance
     */
    private static final class LogWrapper {
        //~ Members --------------------------------------------------------------------------------------------------------

        /** parent CommonsLoggingSessionLog instance */
        private final CommonsLoggingSessionLog sessionLog;
        /** category for debug mode */
        private final String category;
        /** apache commons logging Log instance */
        private final Log log;
        /** parent LogWrapper */
        private final LogWrapper parent;
        /** child LogWrapper instances */
        private List<LogWrapper> children = null;
        /** level as defined by java.util.logging.Levels. Can be changed at runtime */
        private int level = UNDEFINED_LEVEL;
        /** cached level as defined by java.util.logging.Levels. Extracted from parent LogWrapper instances */
        private int cachedLevel = UNDEFINED_LEVEL;

        //~ Constructors ---------------------------------------------------------------------------------------------------
        /**
         * INTERNAL:
         *
         * Constructor
         *
         * @param log apache commons logging Log instance
         */
        protected LogWrapper(final CommonsLoggingSessionLog sessionLog, final String category, final Log log) {
            this.sessionLog = sessionLog;
            this.category = category;
            this.log = log;

            final Logger logger = CommonsLoggingSessionLog.getLog4JLogger(log);

            String parentName = null;

            if (logger != null) {
                parentName = logger.getParent().getName();
            }

            if ((parentName != null) && !"null".equals(parentName)) {
                if (FORCE_INTERNAL_DEBUG) {
                    debug("CommonsLoggingSessionLog.LogWrapper.new : parent : " + parentName);
                }

                this.parent = this.sessionLog.getLogWrapper(parentName);

                if (this.parent != null) {
                    this.parent.addChild(this);
                }
            } else {
                this.parent = null;
            }
        }

        //~ Methods --------------------------------------------------------------------------------------------------------
        /**
         * INTERNAL: Returns the category
         *
         * @return category
         */
        protected final String getCategory() {
            return this.category;
        }

        /**
         * INTERNAL: Reset the cachedLevel
         */
        protected final void resetCachedLevel() {
            this.cachedLevel = UNDEFINED_LEVEL;

            if (FORCE_INTERNAL_DEBUG) {
                debug("CommonsLoggingSessionLog.LogWrapper.setLevel : reset cachedLevel for : " + getCategory());
            }
        }

        /**
         * INTERNAL: Returns the apache commons logging Log instance
         *
         * @return apache commons logging Log instance
         */
        protected final Log getLog() {
            return this.log;
        }

        /**
         * INTERNAL: Returns the level according to the java.util.logging.Levels
         *
         * @return level computed from cachedLevel and internal level
         */
        protected final int getLevel() {
            int res = this.cachedLevel;

            // if the cachedLevel is undefined : compute it from parents :
            if (res == UNDEFINED_LEVEL) {
                // first gives the internal level :
                res = this.level;

                if (FORCE_INTERNAL_DEBUG) {
                    System.out.println(
                            "CommonsLoggingSessionLog.LogWrapper.getLevel : this : " + getCategory() + " : UNCACHED level : " + getLevelString(res));
                }

                if (res == UNDEFINED_LEVEL) {
                    res = computeLevel(UNDEFINED_LEVEL);

                    if (USE_INTERNAL_CACHE) {
                        this.cachedLevel = res;
                    }
                }
            }
            return res;
        }

        /**
         * INTERNAL: Defines the level according to the java.util.logging.Levels
         *
         * @param level value according to the java.util.logging.Levels
         */
        protected final void setLevel(final int level) {
            this.level = level;

            if (USE_INTERNAL_CACHE) {
                this.resetCachedLevel();
                if (this.children != null) {
                    // reset cachedLevel for all children :
                    for (final LogWrapper cw : this.children) {
                        cw.resetCachedLevel();
                    }
                }
            }
        }

        /**
         * INTERNAL: Adds a child LogWrapper
         *
         * @param lw
         */
        protected final void addChild(final LogWrapper lw) {
            if (this.children == null) {
                this.children = new ArrayList<LogWrapper>();
            }

            this.children.add(lw);

            if (FORCE_INTERNAL_DEBUG) {
                System.out.println(
                        "CommonsLoggingSessionLog.LogWrapper.addChild : this : " + getCategory() + " : child : " + lw.getCategory());
            }
        }

        /**
         * INTERNAL: Computes the cached Level
         *
         * @param localLevel this.level copy
         *
         * @return level
         */
        private final int computeLevel(final int localLevel) {
            LogWrapper pw;
            LogWrapper lw = this;

            if (FORCE_INTERNAL_DEBUG) {
                System.out.println(
                        "CommonsLoggingSessionLog.LogWrapper.computeLevel : IN : " + getCategory() + " : level : " +
                        CommonsLoggingSessionLog.getLevelString(localLevel));
            }

            while ((lw != null) &&
                    (((lw == this) && (localLevel == UNDEFINED_LEVEL)) ||
                    ((lw != this) && (lw.getLevel() == UNDEFINED_LEVEL)))) {
                pw = lw.parent;

                if (pw != lw) {
                    lw = pw;
                } else {
                    // exit from loop :
                    lw = null;
                }
            }

            int computedLevel = OFF;

            if (lw != null) {
                computedLevel = lw.getLevel();

                if (FORCE_INTERNAL_DEBUG) {
                    System.out.println(
                            "CommonsLoggingSessionLog.LogWrapper.computeLevel : category : " + lw.getCategory() + " - computedLevel : " + getLevelString(computedLevel) + " : " + lw.getLog());
                }
                final Logger logger = getLog4JLogger(lw.getLog());

                if (logger == null) {
                    error("CommonsLoggingSessionLog.computeLevel : Logger not found : " + lw.getCategory() + " : " + lw.getLog());
                } else {

                    final org.apache.log4j.Level realLevel = getLevelFor(computedLevel);

                    final boolean enabled = logger.isEnabledFor(realLevel);

                    if (!enabled) {
                        computedLevel = OFF;
                    }
                    if (FORCE_INTERNAL_DEBUG) {
                        System.out.println(
                                "CommonsLoggingSessionLog.LogWrapper.computeLevel : category : " + lw.getCategory() + " :  realLevel : " + realLevel + " - enabled = " + enabled);
                    }
                }


            }

            if (FORCE_INTERNAL_DEBUG) {
                System.out.println(
                        "CommonsLoggingSessionLog.LogWrapper.computeLevel : OUT : " + getCategory() + " : level : " +
                        CommonsLoggingSessionLog.getLevelString(computedLevel));
            }

            return computedLevel;
        }
    }
}
//~ End of file --------------------------------------------------------------------------------------------------------
