package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.*
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class MergeStrategyTest {

    @Test
    void testDeepMergeStrategy() {
        def strategy = new DeepMergeStrategy()

        def base = [
                a: 1,
                nested: [x: 1, y: 2],
                list: [1, 2]
        ]
        def override = [
                a: 2,
                nested: [y: 3, z: 4],
                list: [3]
        ]

        def result = strategy.merge(base, override) as Map

        assertEquals(2, result.a)
        assertEquals(1, result.nested.x)
        assertEquals(3, result.nested.y) // Override inside nested
        assertEquals(4, result.nested.z)
        assertEquals([3], result.list) // Default list strategy is Override
    }

    @Test
    void testDeepMergeWithUniqueListStrategy() {
        // 组合策略：Map 深度合并，List 去重追加
        def strategy = new DeepMergeStrategy(new UniqueMergeStrategy())

        def base = [list: [1, 2]]
        def override = [list: [2, 3]]

        def result = strategy.merge(base, override) as Map

        assertEquals([1, 2, 3], result.list)
    }

    @Test
    void testAppendMergeStrategy() {
        def strategy = new AppendMergeStrategy()
        def base = [a: 1]
        def override = [b: 2]

        def result = strategy.merge(base, override) as Map
        assertEquals(1, result.a)
        assertEquals(2, result.b)

        // List append
        assertEquals([1, 2, 3, 4], strategy.merge([1, 2], [3, 4]))
    }

    @Test
    void testConfigMergerFactory() {
        assertTrue(MergeStrategyFactory.getStrategy('deep') instanceof DeepMergeStrategy)
        assertTrue(MergeStrategyFactory.getStrategy('unique') instanceof UniqueMergeStrategy)
        // 测试默认
        assertTrue(MergeStrategyFactory.getStrategy(null) instanceof DeepMergeStrategy)
    }
}