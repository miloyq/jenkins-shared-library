package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.util.MapUtils

/**
 * Facade class providing a semantic API for the pipeline configuration.
 * Hides the underlying Map structure and complex merging logic from the user.
 */
class PipelineConfig implements Serializable {
    private static final long serialVersionUID = 1L

    private Map rawConfig

    PipelineConfig(Map rawConfig) {
        this.rawConfig = rawConfig
    }

    /**
     * Retrieves the 'global' configuration block.
     */
    Map getGlobalConfig() {
        return MapUtils.getNestedMap(rawConfig, 'global')
    }

    /**
     * Retrieves configuration for a specific stage.
     * Merges 'global' config with 'stages.<stageName>'.
     */
    Map getStageConfig(String stageName) {
        return ConfigMerger.mergeAll([:], [
                getGlobalConfig(),
                MapUtils.getNestedMap(rawConfig, 'stages', stageName)
        ]) as Map
    }

    /**
     * Retrieves configuration for a specific step.
     * Merges 'global' -> 'stages.<stageName>' -> 'steps.<stepName>'.
     */
    Map getStepConfig(String stageName, String stepName) {
        return ConfigMerger.mergeAll([:], [
                getStageConfig(stageName),
                MapUtils.getNestedMap(rawConfig, 'steps', stepName)
        ]) as Map
    }

    /**
     * Retrieves step configuration with dynamic argument overrides.
     * Allows pipeline step arguments to override static configuration files.
     *
     * @param args Arguments passed to the pipeline step.
     * @param argMapping A map defining how arguments map to config keys (argKey -> configPath).
     * @param stageName The current stage name.
     * @param stepName The current step name.
     * @return The final configuration map for the step.
     */
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
                        false
                )
            } else {
                stepConfig[k] = v
            }
        }

        return stepConfig
    }

    /**
     * Retrieves the 'post' configuration block.
     */
    Map getPostConfig() {
        return MapUtils.getNestedMap(rawConfig, 'post')
    }

    /**
     * Returns the raw underlying configuration map.
     */
    Map getRawConfig() {
        return rawConfig
    }
}