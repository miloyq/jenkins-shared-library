package io.github.miloyq.jenkins.config.strategy

import io.github.miloyq.jenkins.config.MergeStrategy

class MergeStrategyFactory {
    static MergeStrategy getStrategy(String name) {
        switch (name?.toLowerCase()) {
            case 'override':
                return new OverrideMergeStrategy()
            case 'append':
                return new AppendMergeStrategy()
            case 'unique':
                return new UniqueMergeStrategy()
            case 'deep':
                return new DeepMergeStrategy()
            default:
                return new DeepMergeStrategy()
        }
    }
}