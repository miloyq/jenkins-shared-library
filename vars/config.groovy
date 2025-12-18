import io.github.miloyq.jsl.config.ConfigLoader
import io.github.miloyq.jsl.config.MergeStrategyFactory
import io.github.miloyq.jsl.config.PipelineConfig

def load(Map args = [:]) {
    def files = (args.files instanceof List)
            ? args.files as List
            : [args.files]
    def strategy = MergeStrategyFactory.getStrategy(args.strategy as String)

    def loader = new ConfigLoader(this)
    def rawConfig = loader.loadConfig(files, strategy)
    def pipelineConfig = new PipelineConfig(rawConfig)
    // 注册到 Jenkins Pipeline Script Binding
    this.binding.setVariable("PIPELINE_CONFIG", pipelineConfig)

    return rawConfig
}