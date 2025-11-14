package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.DeepMergeStrategy

class ConfigMerger {
    static Object merge(Object base, Object override, MergeStrategy strategy = new DeepMergeStrategy()) {
        return strategy.merge(base, override)
    }
}