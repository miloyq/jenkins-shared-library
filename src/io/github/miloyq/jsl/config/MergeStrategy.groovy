package io.github.miloyq.jsl.config

/**
 * Interface defining how two objects (usually Maps or Lists) should be merged.
 */
interface MergeStrategy {
    /**
     * Merges an override object into a base object.
     *
     * @param base The original object
     * @param override The object containing changes
     * @return The result of the merge
     */
    Object merge(Object base, Object override)
}