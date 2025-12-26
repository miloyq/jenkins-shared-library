package io.github.miloyq.jsl.config.strategy

/**
 * Unique merge strategy.
 *
 * Behavior:
 * - Maps: Shallow merge (base + override).
 * - Lists: Concatenates lists and removes duplicates.
 */
class UniqueMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMap(Map base, Map override) {
        return base + override
    }

    @Override
    List mergeList(List base, List override) {
        return (base + override).unique()
    }
}