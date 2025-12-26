package io.github.miloyq.jsl.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for PipelineConfig.
 * Verifies hierarchical configuration retrieval and argument mapping.
 */
class PipelineConfigTest {
    private PipelineConfig pipelineConfig

    @BeforeEach
    void setup() {
        def rawConfig = [
                global: [
                        registry: 'docker.io'
                ],
                stages: [
                        build: [
                                image: 'maven:3.8'
                        ]
                ],
                steps: [
                        customStep: [
                                timeout: 30
                        ]
                ]
        ]

        pipelineConfig = new PipelineConfig(rawConfig)
    }

    @Test
    void testGetGlobalConfig() {
        assertEquals('docker.io', pipelineConfig.globalConfig.registry)
    }

    /**
     * Tests that stage configuration correctly inherits from global configuration.
     */
    @Test
    void testGetStageConfigMerge() {
        def config = pipelineConfig.getStageConfig('build')

        assertEquals('docker.io', config.registry) // Inherited from global
        assertEquals('maven:3.8', config.image)    // Defined in stage
    }

    /**
     * Tests the complex scenario of mapping pipeline step arguments to config values.
     * Verifies that 'args' can override static configuration.
     */
    @Test
    void testGetStepConfigWithArgsMapping() {
        // Prepare arguments passed from the pipeline step (e.g., myStep(myImage: '...'))
        def args = [
                myImage: 'golang:1.16',
                myScript: 'go build'
        ]

        // Define mapping: Argument 'myImage' maps to config key 'image'
        // Argument 'myScript' maps to a deeply nested key 'steps.sh.script'
        def mapping = [
                myImage: 'image',
                myScript: 'steps.sh.script' // Nested mapping
        ]

        // Execute: Retrieve configuration with mapping applied
        def config = pipelineConfig.getStepConfig(args, mapping, 'build', 'customStep')

        // Verify: Arguments should override configuration at the mapped paths
        assertEquals('golang:1.16', config.image) // From args mapping override
        assertEquals('go build', config.steps.sh.script) // From args nested mapping
        assertEquals(30, config.timeout) // From steps config
        assertEquals('docker.io', config.registry) // From global
    }
}