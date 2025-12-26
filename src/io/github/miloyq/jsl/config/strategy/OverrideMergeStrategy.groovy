package io.github.miloyq.jsl.config.strategy

/**
 * Override merge strategy.
 *
 * Behavior:
 * - Maps: The override map completely replaces the base map.
 * - Lists: The override list completely replaces the base list.
 */
class OverrideMergeStrategy extends BaseMergeStrategy {
    @Override
    Map mergeMap(Map base, Map override) {
        return override
    }

    @Override
    List mergeList(List base, List override) {
        return override
    }
}