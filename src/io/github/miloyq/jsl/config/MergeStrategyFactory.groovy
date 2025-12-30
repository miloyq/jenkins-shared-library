package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.AppendMergeStrategy
import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy
import io.github.miloyq.jsl.config.strategy.OverrideMergeStrategy
import io.github.miloyq.jsl.config.strategy.UniqueMergeStrategy

/**
 * Factory class to instantiate MergeStrategy objects based on string identifiers.
 */
class MergeStrategyFactory {
    /**
     * Creates a MergeStrategy instance.
     *
     * @param name The name of the strategy (case-insensitive):
     * - 'override': Returns OverrideMergeStrategy
     * - 'append': Returns AppendMergeStrategy
     * - 'unique': Returns UniqueMergeStrategy
     * - 'deep': Returns DeepMergeStrategy (default)
     * @param options Configuration options for complex strategies.
     * - listStrategy: (For 'deep' strategy) Name of the strategy to use for lists.
     * @return A concrete MergeStrategy instance.
     */
    static MergeStrategy getStrategy(String name, Map options = [:]) {
        switch (name?.toLowerCase()) {
            case 'override':
                return new OverrideMergeStrategy()
            case 'append':
                return new AppendMergeStrategy()
            case 'unique':
                return new UniqueMergeStrategy()
            case 'deep':
                String listStrategyName = options?.listStrategy ?: 'override'
                def listStrategy = getStrategy(listStrategyName)
                return new DeepMergeStrategy(listStrategy)
            default:
                return new DeepMergeStrategy()
        }
    }
}