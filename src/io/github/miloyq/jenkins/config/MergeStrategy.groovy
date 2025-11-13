package io.github.miloyq.jenkins.config

interface MergeStrategy {
    Object merge(Object base, Object override)
}