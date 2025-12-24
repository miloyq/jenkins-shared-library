package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.util.MapUtils

class PipelineConfig implements Serializable {
    private static final long serialVersionUID = 1L

    private Map rawConfig

    PipelineConfig(Map rawConfig) {
        this.rawConfig = rawConfig
    }

    Map getGlobalConfig() {
        MapUtils.getNestedMap(rawConfig, 'global')
    }

    Map getStageConfig(String stageName) {
        ConfigMerger.mergeAll(
                [:],
                [
                        getGlobalConfig(),
                        MapUtils.getNestedMap(rawConfig, 'stages', stageName)
                ]
        ) as Map
    }

    Map getStepConfig(String stageName, String stepName) {
        ConfigMerger.mergeAll(
                [:],
                [
                        getStageConfig(stageName),
                        MapUtils.getNestedMap(rawConfig, 'steps', stepName)
                ]
        ) as Map
    }

    Map getStepConfig(
            Map args,
            Map argMapping,
            String stageName,
            String stepName
    ) {
        def stepConfig = getStepConfig(stageName, stepName)

        args?.each { k, v ->
            if (k == 'stageName') return

            if (argMapping?.containsKey(k)) {
                MapUtils.setNestedValue(
                        stepConfig,
                        argMapping[k] as String,
                        v,
                        true
                )
            } else if (stepConfig.containsKey(k)) {
                stepConfig[k] = v
            }
        }

        stepConfig
    }

    Map getPostConfig() {
        MapUtils.getNestedMap(rawConfig, 'post')
    }

    Map getRawConfig() {
        return rawConfig
    }
}