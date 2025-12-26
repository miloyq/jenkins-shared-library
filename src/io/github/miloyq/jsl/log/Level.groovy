package io.github.miloyq.jsl.log

/**
 * Log severity levels.
 */
enum Level {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4)

    final int priority

    Level(int priority) {
        this.priority = priority
    }

    /**
     * Resolves a Level enum from a string name (case-insensitive).
     * Defaults to INFO if the name is invalid.
     */
    static Level from(String name) {
        try {
            return valueOf(name?.toUpperCase() ?: 'INFO')
        } catch (ignored) {
            return INFO
        }
    }
}