package io.github.miloyq.jsl.util

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class MapUtilsTest {

    @Test
    void testGetNestedValue() {
        def map = [a: [b: [c: 'value']]]
        assertEquals('value', MapUtils.getNestedValue(map, 'a', 'b', 'c'))
        assertNull(MapUtils.getNestedValue(map, 'a', 'x'))
        assertNull(MapUtils.getNestedValue(map, 'z'))
    }

    @Test
    void testSetNestedValue() {
        def map = [:]

        // 测试自动创建层级
        assertTrue(MapUtils.setNestedValue(map, ['a', 'b'], 100))
        assertEquals(100, map.a.b)

        // 测试 strict 模式：路径中间节点不是 Map 时应失败
        map = [a: [b: "leaf"]]
        assertFalse(MapUtils.setNestedValue(map, ['a', 'b', 'c'], 1, true))

        // 测试非 strict 模式：覆盖非 Map 节点
        assertTrue(MapUtils.setNestedValue(map, ['a', 'b', 'c'], 1, false))
        assertEquals(1, map.a.b.c)
    }

    @Test
    void testExpandFlatMap() {
        def flat = [
                'app.name'   : 'demo',
                'app.version': '1.0',
                'server.port': 8080
        ]

        def expanded = MapUtils.expandFlatMap(flat)

        assertEquals('demo', expanded.app.name)
        assertEquals(8080, expanded.server.port)
    }
}