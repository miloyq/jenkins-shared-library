package io.github.miloyq.jsl.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

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

    @Test
    void testGetStageConfigMerge() {
        // Stage 配置应该包含 global 配置
        def config = pipelineConfig.getStageConfig('build')
        assertEquals('docker.io', config.registry)
        assertEquals('maven:3.8', config.image)
    }

    @Test
    void testGetStepConfigWithArgsMapping() {
        // 测试带有参数映射的方法
        def args = [
                myImage: 'golang:1.16',
                myScript: 'go build'
        ]
        def mapping = [
                myImage: 'image',
                myScript: 'steps.sh.script' // 嵌套映射
        ]

        // 基础配置：global + stages(build) + steps(customStep)
        // 预期结果应合并所有层级，并应用 args 映射
        def config = pipelineConfig.getStepConfig(args, mapping, 'build', 'customStep')

        assertEquals('golang:1.16', config.image) // 来自 args 映射覆盖
        assertEquals('go build', config.steps.sh.script) // 来自 args 嵌套映射
        assertEquals(30, config.timeout) // 来自 steps 配置
        assertEquals('docker.io', config.registry) // 来自 global
    }
}