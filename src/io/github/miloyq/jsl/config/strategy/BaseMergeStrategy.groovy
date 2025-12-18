package io.github.miloyq.jsl.config.strategy

import io.github.miloyq.jsl.config.MergeStrategy

abstract class BaseMergeStrategy implements MergeStrategy {
    @Override
    Object merge(Object base, Object override) {
        if (!base) return override
        if (!override) return base

        if (base instanceof Map && override instanceof Map) {
            return mergeMap(base, override)
        } else if (base instanceof List && override instanceof List) {
            return mergeList(base, override)
        }
        return override
    }

    abstract Map mergeMap(Map base, Map override)

    abstract List mergeList(List base, List override)
}