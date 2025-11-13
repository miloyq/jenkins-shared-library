package io.github.miloyq.jenkins.log

import io.github.miloyq.jenkins.util.JenkinsUtils

class Logger implements Serializable {
    private def script
    private Level logLevel
    private String scope

    Logger(script = null, Level logLevel = null, String scope = null) {
        this.script = script
        this.logLevel = resolveLogLevel(script, logLevel)
        this.scope = resolveScope(script, scope)
    }

    void debug(String msg) { log(Level.DEBUG, msg, Color.CYAN) }

    void info(String msg) { log(Level.INFO, msg, Color.GREEN) }

    void warn(String msg) { log(Level.WARN, msg, Color.YELLOW) }

    void error(String msg) { log(Level.ERROR, msg, Color.RED) }

    private void log(Level msgLevel, String msg, Color color) {
        if (!shouldLog(msgLevel)) return
        def formatted = format(msg, msgLevel, color)

        if (script) { // 在 Jenkins pipeline 环境中
            if (msgLevel == Level.ERROR) {
                script.error(formatted)
            } else {
                script.echo(formatted)
            }
        } else { // 降级模式：普通控制台输出
            if (msgLevel == Level.ERROR) {
                throw new RuntimeException(formatted)
            } else {
                println(formatted)
            }
        }
    }

    private boolean shouldLog(Level msgLevel) {
        return msgLevel.priority >= logLevel.priority
    }

    private static Level resolveLogLevel(script, Level logLevel) {
        // 优先级：显式参数 > Jenkins env > 系统环境变量 > 默认 INFO
        if (logLevel) return logLevel

        String rawLevel = null
        if (script?.env?.LOG_LEVEL) {
            rawLevel = script.env.LOG_LEVEL
        } else if (System.getenv('LOG_LEVEL')) {
            rawLevel = System.getenv('LOG_LEVEL')
        }

        return Level.from(rawLevel ?: 'INFO')
    }

    private static String resolveScope(script, String scope) {
        if (scope) return scope
        return JenkinsUtils.resolveScriptName(script)
    }

    private String format(String msg, Level level, Color color) {
        return "${color.code}[${scope}] ${level.name()}: ${msg}${Color.RESET.code}"
    }
}