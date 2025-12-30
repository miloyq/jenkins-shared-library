package io.github.miloyq.jsl.config.strategy

import io.github.miloyq.jsl.config.MergeStrategy

/**
 * Deep merge strategy.
 *
 * Behavior:
 * - Maps: Recursively merged. Keys in 'override' update 'base'.
 * - Lists: Delegated to a secondary strategy (default: Override).
 */
class DeepMergeStrategy extends BaseMergeStrategy {
    private MergeStrategy listStrategy

    /**
     * @param listStrategy The strategy to use when encountering Lists (Default: OverrideMergeStrategy)
     */
    DeepMergeStrategy(
            MergeStrategy listStrategy = new OverrideMergeStrategy()
    ) {
        this.listStrategy = listStrategy
    }

    @Override
    Map mergeMap(Map base, Map override) {
        def merged = [:] + base

        override.each { k, v ->
            merged[k] = merged.containsKey(k)
                    ? merge(merged[k], v)
                    : v
        }

        return merged
    }

    @Override
    List mergeList(List base, List override) {
        return listStrategy?.merge(base, override) as List
    }
}