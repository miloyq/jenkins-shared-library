package io.github.miloyq.jsl.config

import io.github.miloyq.jsl.config.strategy.*
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for various MergeStrategy implementations.
 */
class MergeStrategyTest {

    /**
     * Tests the DeepMergeStrategy logic.
     * Verifies that maps are merged recursively and lists use the default override strategy.
     */
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

        // Verify Map merging logic
        assertEquals(2, result.a)
        assertEquals(1, result.nested.x) // Preserved from base
        assertEquals(3, result.nested.y) // Overridden by override
        assertEquals(4, result.nested.z) // Added from override

        // Verify List merging logic (Default list strategy is Override)
        assertEquals([3], result.list)
    }

    /**
     * Tests DeepMergeStrategy composed with UniqueMergeStrategy for lists.
     */
    @Test
    void testDeepMergeWithUniqueListStrategy() {
        // Composition: Deep merge for Maps, Unique merge for Lists
        def strategy = new DeepMergeStrategy(new UniqueMergeStrategy())

        def base = [list: [1, 2]]
        def override = [list: [2, 3]]

        def result = strategy.merge(base, override) as Map

        // Verify: Lists should be combined and deduplicated
        assertEquals([1, 2, 3], result.list)
    }

    /**
     * Tests AppendMergeStrategy.
     * Verifies shallow map merging and list concatenation.
     */
    @Test
    void testAppendMergeStrategy() {
        def strategy = new AppendMergeStrategy()
        def base = [a: 1]
        def override = [b: 2]

        def result = strategy.merge(base, override) as Map

        // Verify: Simple shallow merge for Maps
        assertEquals(1, result.a)
        assertEquals(2, result.b)

        // Verify: List append behavior (allows duplicates)
        assertEquals([1, 2, 3, 4], strategy.merge([1, 2], [3, 4]))
    }

    /**
     * Tests the Factory class to ensure it returns correct instances.
     */
    @Test
    void testConfigMergerFactory() {
        assertTrue(MergeStrategyFactory.getStrategy('deep') instanceof DeepMergeStrategy)
        assertTrue(MergeStrategyFactory.getStrategy('unique') instanceof UniqueMergeStrategy)

        // Verify default fallback
        assertTrue(MergeStrategyFactory.getStrategy(null) instanceof DeepMergeStrategy)
    }
}