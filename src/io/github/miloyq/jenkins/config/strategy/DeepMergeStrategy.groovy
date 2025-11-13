package io.github.miloyq.jenkins.config.strategy

import io.github.miloyq.jenkins.config.MergeStrategy

class DeepMergeStrategy extends AbstractMergeStrategy {
    private MergeStrategy listStrategy

    DeepMergeStrategy(MergeStrategy listStrategy = new OverrideMergeStrategy()) {
        this.listStrategy = listStrategy
    }

    @Override
    Map mergeMaps(Map base, Map override) {
        def merged = [:] + base
        override.each { k, v ->
            merged[k] = merged.containsKey(k)
                    ? merge(merged[k], v)
                    : v
        }
        return merged
    }

    @Override
    List mergeLists(List base, List override) {
        return listStrategy.merge(base, override) as List
    }
}