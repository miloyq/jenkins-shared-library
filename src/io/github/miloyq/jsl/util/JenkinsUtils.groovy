package io.github.miloyq.jsl.util

class JenkinsUtils implements Serializable {
    static String resolveScriptName(script, String defaultName = 'Pipeline') {
        try {
            def clazz = script?.getClass()
            def name = clazz?.simpleName
            if (name && !name.endsWith('WorkflowScript') && !name.contains('CpsScript')) {
                return name
            }
        } catch (ignored) {
        }
        return script?.env?.JOB_NAME ?: defaultName
    }
}