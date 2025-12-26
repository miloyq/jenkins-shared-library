import io.github.miloyq.jsl.config.ConfigLoader
import io.github.miloyq.jsl.config.MergeStrategyFactory
import io.github.miloyq.jsl.config.PipelineConfig
import io.github.miloyq.jsl.log.Logger

/**
 * Global entry point for loading pipeline configuration.
 *
 * Implements a Singleton pattern via the Jenkins binding to ensure configuration
 * is parsed only once per build, improving performance.
 *
 * Usage:
 * def config = loadConfig(files: ['jenkins.yml'], strategy: 'override')
 *
 * @param args Configuration map:
 * - files (List|String): Path(s) to configuration files relative to workspace root.
 * - strategy (String): Merge strategy name [deep, override, append, unique]. Default: 'deep'.
 * - options (Map): Additional options for the strategy factory (e.g., listStrategy).
 * @return Map The raw merged configuration map.
 */
def call(Map args = [:]) {
    def log = new Logger(this, 'loadConfig')

    if (this.binding.hasVariable("PIPELINE_CONFIG")) {
        log.info("PIPELINE_CONFIG already loaded. Using cached version.")
        def pipelineConfig = this.binding.getVariable("PIPELINE_CONFIG") as PipelineConfig
        return pipelineConfig.getRawConfig()
    }

    def files = (args.files instanceof List)
            ? args.files as List
            : [args.files]
    def strategy = MergeStrategyFactory.getStrategy(
            args.strategy as String,
            args.options as Map
    )

    def loader = new ConfigLoader(this)
    def rawConfig = loader.loadConfig(files, strategy)
    def pipelineConfig = new PipelineConfig(rawConfig)

    // Registers the config object to the script binding for global access
    this.binding.setVariable("PIPELINE_CONFIG", pipelineConfig)

    return rawConfig
}