import io.github.miloyq.jsl.config.ConfigLoader
import io.github.miloyq.jsl.config.MergeStrategyFactory
import io.github.miloyq.jsl.config.PipelineConfig
import io.github.miloyq.jsl.log.Logger

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

    // Register the configuration object to the Jenkins Pipeline Script Binding
    // so it can be accessed globally via the PIPELINE_CONFIG variable.
    this.binding.setVariable("PIPELINE_CONFIG", pipelineConfig)

    return rawConfig
}