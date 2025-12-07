package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy
import io.github.miloyq.jsl.log.Logger

class ConfigLoader implements Serializable {
    private static final String DEFAULT_CONFIG_PATH = 'config/default.yml'

    private def script
    private Logger log

    ConfigLoader(script) {
        this.script = script
        this.log = new Logger(script, 'ConfigLoader')
    }

    Map loadConfig(List files = [], MergeStrategy strategy = new DeepMergeStrategy()) {
        def merged = loadDefaultConfig(DEFAULT_CONFIG_PATH)
        files?.each { file ->
            def current = readConfig(file as String)
            merged = ConfigMerger.merge(merged, current, strategy) as Map
        }
        return merged
    }

    private Map loadDefaultConfig(String path) {
        try {
            def text = script.libraryResource(path)
            return script.readYaml(text: text) ?: [:]
        } catch (err) {
            log.error("Failed to get resource(${DEFAULT_CONFIG_PATH}): ${err.message}")
        }
    }

    private Map readConfig(String file) {
        if (!script.fileExists(file)) {
            log.warn("Config file not found: ${file}")
            return [:]
        }

        def ext = file.substring(file.lastIndexOf('.') + 1).toLowerCase()
        try {
            switch (ext) {
                case ['yaml', 'yml']:
                    return script.readYaml(file: file)
                case 'json':
                    return script.readJSON(file: file)
                case 'properties':
                    return script.readProperties(file: file)
                default:
                    log.warn("Unsupported config file type: ${file}")
                    return [:]
            }
        } catch (err) {
            log.warn("Failed to read config ${file}: ${err.message}")
            return [:]
        }
    }
}