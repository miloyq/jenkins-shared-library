package io.github.miloyq.jsl.log

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the Logger utility.
 */
class LoggerTest {

    /**
     * Mocks the Jenkins script to capture echo/error output.
     */
    class MockLoggerScript {
        String lastMessage
        boolean errorCalled = false
        def env = [:]

        void echo(String msg) { lastMessage = msg }

        void error(String msg) {
            lastMessage = msg
            errorCalled = true
        }
    }

    /**
     * Verifies that the Logger respects priority levels (e.g., INFO < WARN).
     */
    @Test
    void testLogLevelPriority() {
        def script = new MockLoggerScript()
        // Setup: Set log level to WARN
        def logger = new Logger(script, "TestScope", Level.WARN)

        logger.info("Info message")
        assertNull(script.lastMessage) // Info < Warn, should not log

        logger.warn("Warn message")
        assertTrue(script.lastMessage.contains("Warn message"))

        // Reset and test Error level
        script.lastMessage = null
        logger.error("Error message")
        assertTrue(script.lastMessage.contains("Error message"))
        assertTrue(script.errorCalled)
    }

    /**
     * Verifies correct string formatting, including scope labels and ANSI colors.
     */
    @Test
    void testScopeAndFormatting() {
        def script = new MockLoggerScript()
        def logger = new Logger(script, "MyScope", Level.INFO)

        logger.info("Hello")

        // Verify format: [Scope] Level: Message
        assertTrue(script.lastMessage.contains("[MyScope]"))
        assertTrue(script.lastMessage.contains("INFO"))
        assertTrue(script.lastMessage.contains("Hello"))

        // Verify presence of ANSI color codes
        assertTrue(script.lastMessage.contains("\u001B["))
    }
}