package io.github.miloyq.jenkins.config

import io.github.miloyq.jenkins.config.strategy.DeepMergeStrategy

class ConfigMerger {
    static Object merge(Object base, Object override, MergeStrategy strategy = new DeepMergeStrategy()) {
        return strategy.merge(base, override)
    }
}