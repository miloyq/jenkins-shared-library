package io.github.miloyq.jsl.config.strategy

class UniqueMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMap(Map base, Map override) {
        base + override
    }

    @Override
    List mergeList(List base, List override) {
        (base + override).unique()
    }
}