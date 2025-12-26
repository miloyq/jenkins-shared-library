package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy

/**
 * Utility class for executing merge operations.
 */
class ConfigMerger {
    /**
     * Merges two objects using the specified strategy.
     */
    static Object merge(
            Object base,
            Object override,
            MergeStrategy strategy = new DeepMergeStrategy()
    ) {
        return strategy?.merge(base, override)
    }

    /**
     * Merges a list of overrides into a base object sequentially.
     *
     * @param base The initial base object.
     * @param overrides A list of objects to merge on top of the base.
     * @param strategy The strategy to use.
     * @return The final merged result.
     */
    static Object mergeAll(
            Object base,
            List overrides,
            MergeStrategy strategy = new DeepMergeStrategy()
    ) {
        return overrides?.inject(base) { acc, o ->
            strategy?.merge(acc, o)
        }
    }
}