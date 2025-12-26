package io.github.miloyq.jsl.util

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for MapUtils.
 */
class MapUtilsTest {

    @Test
    void testGetNestedValue() {
        def map = [a: [b: [c: 'value']]]

        // Verify successful retrieval
        assertEquals('value', MapUtils.getNestedValue(map, 'a', 'b', 'c'))

        // Verify null for non-existent paths
        assertNull(MapUtils.getNestedValue(map, 'a', 'x'))
        assertNull(MapUtils.getNestedValue(map, 'z'))
    }

    /**
     * Tests setting nested values in both strict and non-strict modes.
     */
    @Test
    void testSetNestedValue() {
        def map = [:]

        // Test: Automatically create nested structure
        assertTrue(MapUtils.setNestedValue(map, ['a', 'b'], 100))
        assertEquals(100, map.a.b)

        // Test: Strict mode - should fail if intermediate node is not a Map (e.g., 'b' is a String)
        map = [a: [b: "leaf"]]
        assertFalse(MapUtils.setNestedValue(
                map,
                ['a', 'b', 'c'],
                1,
                true
        ))

        // Test: Non-strict mode - should overwrite non-Map nodes
        assertTrue(MapUtils.setNestedValue(
                map,
                ['a', 'b', 'c'],
                1,
                false
        ))
        assertEquals(1, map.a.b.c)
    }

    /**
     * Tests expanding a flat map (dot notation) into a nested structure.
     */
    @Test
    void testExpandFlatMap() {
        def flat = [
                'app.name'   : 'demo',
                'app.version': '1.0',
                'server.port': 8080
        ]

        def expanded = MapUtils.expandFlatMap(flat)

        // Verify structure
        assertEquals('demo', expanded.app.name)
        assertEquals(8080, expanded.server.port)
    }
}