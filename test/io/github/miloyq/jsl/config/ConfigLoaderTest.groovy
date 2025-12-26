package io.github.miloyq.jsl.config

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for ConfigLoader.
 * Verifies file reading, parsing, and integration with the default configuration.
 */
class ConfigLoaderTest {

    /**
     * Simulates the Jenkins Pipeline script object (CPS).
     * Mocks file system operations and resource loading.
     */
    class MockScript {
        Map<String, String> fileContents = [:]
        def env = [LOG_LEVEL: 'DEBUG']

        String libraryResource(String path) {
            if (path == 'config/default.yml') return "global: { default: true }"

            throw new Exception("Resource not found: $path")
        }

        Object readYaml(Map args) {
            // Case 1: Parsing raw text string
            if (args.text) return [global: [default: true]]

            // Case 2: Reading from a file in the workspace
            String file = args.file
            if (fileContents.containsKey(file)) {
                if (file.endsWith('yml')) return [custom: fileContents[file]]
            }
            return null
        }

        boolean fileExists(String file) {
            return fileContents.containsKey(file)
        }

        void echo(String msg) { println "ECHO: $msg" }
        void error(String msg) { println "ERROR: $msg" }
    }

    /**
     * Tests that a user-provided config file is correctly loaded and merged
     * with the default library resource.
     */
    @Test
    void testLoadConfigWithMerge() {
        def script = new MockScript()
        // Setup: Mock a project-specific config file existing in the workspace
        script.fileContents['project.yml'] = 'value-from-file'

        ConfigLoader loader = new ConfigLoader(script)

        // Execute: Load config specifying the project file.
        def config = loader.loadConfig(['project.yml'])

        // Verify: Result should contain both default values and file overrides
        assertTrue(config.global.default)
        assertEquals('value-from-file', config.custom)
    }

    /**
     * Tests behavior when a requested config file does not exist.
     * The loader should warn but continue, returning at least the default config.
     */
    @Test
    void testLoadConfigFileNotFound() {
        def script = new MockScript()
        ConfigLoader loader = new ConfigLoader(script)

        // Execute: Try to load a non-existent file
        def config = loader.loadConfig(['non-existent.yml'])

        // Verify: It should gracefully handle the error and return the base config (default)
        assertTrue(config.global.default)
    }
}