package io.github.miloyq.jsl.log

/**
 * A context-aware logger for Jenkins pipelines.
 * * Features:
 * - Supports log levels (DEBUG, INFO, WARN, ERROR).
 * - Automatic coloring of output.
 * - Automatically resolves scope (e.g. from STEP_NAME env var).
 * - Graceful degradation: works in both Pipeline context (script.echo) and standard Groovy (println).
 */
class Logger implements Serializable {
    private static final long serialVersionUID = 1L

    private def script
    private String scope
    private Level logLevel

    /**
     * @param script The pipeline script context.
     * @param scope The logging scope/tag. Defaults to 'STEP_NAME' env var if null.
     * @param logLevel Explicit log level. If null, reads from env.LOG_LEVEL.
     */
    Logger(
            script = null,
            String scope = null,
            Level logLevel = null
    ) {
        this.script = script
        this.scope = resolveScope(script, scope)
        this.logLevel = resolveLogLevel(script, logLevel)
    }

    void debug(String msg) { log(Level.DEBUG, msg, Color.CYAN) }

    void info(String msg) { log(Level.INFO, msg, Color.GREEN) }

    void warn(String msg) { log(Level.WARN, msg, Color.YELLOW) }

    void error(String msg) { log(Level.ERROR, msg, Color.RED) }

    private void log(
            Level level,
            String msg,
            Color color
    ) {
        if (!shouldLog(level)) return
        def formatted = format(msg, level, color)

        if (script) {
            if (level == Level.ERROR) {
                script.error(formatted)
            } else {
                script.echo(formatted)
            }
        } else {
            if (level == Level.ERROR) {
                throw new RuntimeException(formatted)
            } else {
                println(formatted)
            }
        }
    }

    private boolean shouldLog(Level level) {
        return level.priority >= logLevel.priority
    }

    private static Level resolveLogLevel(script, Level logLevel) {
        if (logLevel) return logLevel

        String rawLevel = script?.env?.LOG_LEVEL
                ?: System.getenv('LOG_LEVEL')
                ?: 'INFO'

        return Level.from(rawLevel ?: 'INFO')
    }

    private static String resolveScope(script, String scope) {
        if (scope) return scope

        return script?.STEP_NAME ?: 'Unknown'
    }

    private String format(
            String msg,
            Level level,
            Color color
    ) {
        return "${color.code}[${scope}] ${level.name()}: ${msg}${Color.RESET.code}"
    }
}