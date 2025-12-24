package io.github.miloyq.jsl.config

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class ConfigLoaderTest {

    // 模拟 Jenkins Script 对象
    class MockScript {
        Map<String, String> fileContents = [:]

        // 模拟 libraryResource
        String libraryResource(String path) {
            if (path == 'config/default.yml') return "global: { default: true }"
            throw new Exception("Resource not found: $path")
        }

        // 模拟 readYaml
        Object readYaml(Map args) {
            if (args.text) return [global: [default: true]] // 简化解析
            String file = args.file
            if (fileContents.containsKey(file)) {
                // 这里简单返回预设的 Map，实际项目可引入 SnakeYAML 解析字符串
                if (file.endsWith('yml')) return [custom: fileContents[file]]
            }
            return null
        }

        // 模拟 fileExists
        boolean fileExists(String file) {
            return fileContents.containsKey(file)
        }

        // 模拟日志
        def env = [LOG_LEVEL: 'DEBUG']
        void echo(String msg) { println "ECHO: $msg" }
        void error(String msg) { println "ERROR: $msg" }
    }

    @Test
    void testLoadConfigWithMerge() {
        def script = new MockScript()
        script.fileContents['project.yml'] = 'value-from-file'

        ConfigLoader loader = new ConfigLoader(script)

        // 加载项目配置，应与默认配置合并
        def config = loader.loadConfig(['project.yml'])

        assertTrue(config.global.default) // 来自默认配置
        assertEquals('value-from-file', config.custom) // 来自文件
    }

    @Test
    void testLoadConfigFileNotFound() {
        def script = new MockScript()
        ConfigLoader loader = new ConfigLoader(script)

        def config = loader.loadConfig(['non-existent.yml'])

        // 文件不存在不应报错，应返回基础配置（默认配置）
        assertTrue(config.global.default)
    }
}