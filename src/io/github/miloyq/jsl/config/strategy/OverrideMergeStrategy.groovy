package io.github.miloyq.jsl.config.strategy

class OverrideMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMap(Map base, Map override) {
        override
    }

    @Override
    List mergeList(List base, List override) {
        override
    }
}