import io.github.miloyq.jenkins.config.ConfigLoader
import io.github.miloyq.jenkins.config.strategy.MergeStrategyFactory

def call(Map args = [:]) {
    def files = (args.files instanceof List)
            ? args.files as List
            : [args.files]
    def strategy = MergeStrategyFactory.getStrategy(args.strategy as String)
    def loader = new ConfigLoader(this)
    return loader.loadConfig(files, strategy)
}