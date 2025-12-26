package io.github.miloyq.jsl.config.strategy

import io.github.miloyq.jsl.config.MergeStrategy

/**
 * Abstract base class implementing the Template Method pattern for merging.
 * Handles null checks and type routing (Map vs List).
 */
abstract class BaseMergeStrategy implements MergeStrategy {
    @Override
    Object merge(Object base, Object override) {
        if (base == null) return override
        if (override == null) return base

        if (base instanceof Map && override instanceof Map) {
            return mergeMap(base, override)
        } else if (base instanceof List && override instanceof List) {
            return mergeList(base, override)
        }
        return override
    }

    /**
     * specific logic for merging two Maps.
     */
    abstract Map mergeMap(Map base, Map override)

    /**
     * specific logic for merging two Lists.
     */
    abstract List mergeList(List base, List override)
}