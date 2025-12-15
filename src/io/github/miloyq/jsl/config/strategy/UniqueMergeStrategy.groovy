package io.github.miloyq.jsl.config.strategy

class UniqueMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMaps(Map base, Map override) {
        base + override
    }

    @Override
    List mergeLists(List base, List override) {
        (base + override).unique()
    }
}