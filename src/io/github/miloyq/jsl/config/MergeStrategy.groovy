package io.github.miloyq.jsl.config

interface MergeStrategy {
    Object merge(Object base, Object override)
}