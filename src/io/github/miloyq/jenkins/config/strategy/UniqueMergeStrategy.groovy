package io.github.miloyq.jenkins.config.strategy

class UniqueMergeStrategy extends AbstractMergeStrategy {
    @Override
    Map mergeMaps(Map base, Map override) {
        return base + override
    }

    @Override
    List mergeLists(List base, List override) {
        return (base + override).unique()
    }
}