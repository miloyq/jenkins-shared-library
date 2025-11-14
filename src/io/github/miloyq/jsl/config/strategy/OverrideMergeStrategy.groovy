package io.github.miloyq.jsl.config.strategy

class OverrideMergeStrategy extends AbstractMergeStrategy {
    @Override
    Map mergeMaps(Map base, Map override) {
        return override
    }

    @Override
    List mergeLists(List base, List override) {
        return override
    }
}