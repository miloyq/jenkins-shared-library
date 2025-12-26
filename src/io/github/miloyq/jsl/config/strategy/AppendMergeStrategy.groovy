package io.github.miloyq.jsl.config.strategy

/**
 * Append merge strategy.
 *
 * Behavior:
 * - Maps: Shallow merge (base + override).
 * - Lists: Appends elements from override to base. Allows duplicates.
 */
class AppendMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMap(Map base, Map override) {
        return base + override
    }

    @Override
    List mergeList(List base, List override) {
        return base + override
    }
}