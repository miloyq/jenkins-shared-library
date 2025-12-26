package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy
import io.github.miloyq.jsl.log.Logger
import io.github.miloyq.jsl.util.MapUtils

/**
 * Handles the loading and parsing of configuration files.
 * Supports YAML, JSON, and Properties formats.
 */
class ConfigLoader implements Serializable {
    private static final long serialVersionUID = 1L
    private static final String DEFAULT_CONFIG_PATH = 'config/default.yml'

    private def script
    private Logger log

    /**
     * @param script The Jenkins pipeline script context (usually 'this').
     */
    ConfigLoader(script) {
        this.script = script
        this.log = new Logger(script, 'ConfigLoader')
    }

    /**
     * Loads a list of configuration files and merges them with the default configuration.
     *
     * @param files List of file paths to load.
     * @param strategy The merge strategy to apply (default: DeepMergeStrategy).
     * @return The fully merged configuration map.
     */
    Map loadConfig(
            List files = [],
            MergeStrategy strategy = new DeepMergeStrategy()
    ) {
        def merged = loadDefaultConfig(DEFAULT_CONFIG_PATH)

        files?.each { file ->
            def current = readConfig(file as String)
            merged = ConfigMerger.merge(merged, current, strategy)
        }

        return merged as Map
    }

    private Map loadDefaultConfig(String path) {
        try {
            def text = script.libraryResource(path)
            return script.readYaml(text: text) ?: [:]
        } catch (err) {
            log.error("Failed to get resource(${DEFAULT_CONFIG_PATH}): ${err.message}")
            return [:]
        }
    }

    private Map readConfig(String file) {
        if (!script.fileExists(file)) {
            log.warn("Config file not found: ${file}")
            return [:]
        }

        def ext = file.tokenize('.').last().toLowerCase()

        try {
            switch (ext) {
                case ['yaml', 'yml']:
                    return script.readYaml(file: file)
                case 'json':
                    return script.readJSON(file: file)
                case 'properties':
                    return MapUtils.expandFlatMap(script.readProperties(file: file) as Map)
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