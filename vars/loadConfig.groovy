import io.github.miloyq.jsl.config.ConfigLoader
import io.github.miloyq.jsl.config.MergeStrategyFactory

def call(Map args = [:]) {
    def files = (args.files instanceof List)
            ? args.files as List
            : [args.files]
    def strategy = MergeStrategyFactory.getStrategy(args.strategy as String)
    def loader = new ConfigLoader(this)
    def config = loader.loadConfig(files, strategy)

    // 注册到 Jenkins Pipeline Script Binding
    this.binding.setVariable("_JSL_CONFIG", config)

    return config
}