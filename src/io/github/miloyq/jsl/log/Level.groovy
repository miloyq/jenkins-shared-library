package io.github.miloyq.jsl.log

enum Level {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4)

    final int priority

    Level(int priority) {
        this.priority = priority
    }

    static Level from(String name) {
        try {
            return valueOf(name?.toUpperCase() ?: 'INFO')
        } catch (ignored) {
            return INFO
        }
    }
}