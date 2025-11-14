package io.github.miloyq.jsl.config


import io.github.miloyq.jsl.config.strategy.AppendMergeStrategy
import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy
import io.github.miloyq.jsl.config.strategy.OverrideMergeStrategy
import io.github.miloyq.jsl.config.strategy.UniqueMergeStrategy

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