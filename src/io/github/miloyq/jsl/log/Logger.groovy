package io.github.miloyq.jsl.log


class Logger implements Serializable {
    private static final long serialVersionUID = 1L

    private def script
    private String scope
    private Level logLevel

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

        if (script) { // 在 Jenkins pipeline 环境中
            if (level == Level.ERROR) {
                script.error(formatted)
            } else {
                script.echo(formatted)
            }
        } else { // 降级模式：普通控制台输出
            if (level == Level.ERROR) {
                throw new RuntimeException(formatted)
            } else {
                println(formatted)
            }
        }
    }

    private boolean shouldLog(Level level) {
        level.priority >= logLevel.priority
    }

    private static Level resolveLogLevel(script, Level logLevel) {
        // 优先级：显式参数 > Jenkins env > 系统环境变量 > 默认 INFO
        if (logLevel) return logLevel

        String rawLevel = script?.env?.LOG_LEVEL
                ?: System.getenv('LOG_LEVEL')
                ?: 'INFO'

        Level.from(rawLevel ?: 'INFO')
    }

    private static String resolveScope(script, String scope) {
        if (scope) return scope
        script?.STEP_NAME ?: 'Unknown'
    }

    private String format(
            String msg,
            Level level,
            Color color
    ) {
        "${color.code}[${scope}] ${level.name()}: ${msg}${Color.RESET.code}"
    }
}