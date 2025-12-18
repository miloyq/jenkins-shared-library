package io.github.miloyq.jsl.config.strategy

import io.github.miloyq.jsl.config.MergeStrategy

class DeepMergeStrategy extends BaseMergeStrategy {
    private MergeStrategy listStrategy

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
        
        merged
    }

    @Override
    List mergeList(List base, List override) {
        listStrategy.merge(base, override) as List
    }
}