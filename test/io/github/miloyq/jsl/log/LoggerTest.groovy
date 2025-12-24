package io.github.miloyq.jsl.log

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class LoggerTest {

    class MockLoggerScript {
        String lastMessage
        boolean errorCalled = false
        def env = [:]

        void echo(String msg) { lastMessage = msg }
        void error(String msg) { lastMessage = msg; errorCalled = true }
    }

    @Test
    void testLogLevelPriority() {
        def script = new MockLoggerScript()
        // 设置级别为 WARN
        def logger = new Logger(script, "TestScope", Level.WARN)

        logger.info("Info message")
        assertNull(script.lastMessage) // Info < Warn，不应输出

        logger.warn("Warn message")
        assertTrue(script.lastMessage.contains("Warn message"))

        script.lastMessage = null
        logger.error("Error message")
        assertTrue(script.lastMessage.contains("Error message"))
        assertTrue(script.errorCalled)
    }

    @Test
    void testScopeAndFormatting() {
        def script = new MockLoggerScript()
        def logger = new Logger(script, "MyScope", Level.INFO)

        logger.info("Hello")
        // 检查格式: [Scope] Level: Message
        assertTrue(script.lastMessage.contains("[MyScope]"))
        assertTrue(script.lastMessage.contains("INFO"))
        assertTrue(script.lastMessage.contains("Hello"))
        // 检查颜色代码是否存在
        assertTrue(script.lastMessage.contains("\u001B["))
    }
}