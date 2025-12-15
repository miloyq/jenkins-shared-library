package io.github.miloyq.jsl.config.strategy

class OverrideMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMaps(Map base, Map override) {
        override
    }

    @Override
    List mergeLists(List base, List override) {
        override
    }
}