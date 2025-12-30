package io.github.miloyq.jsl.util

/**
 * Utility class for safe and convenient Map operations, specifically handling deep nesting.
 */
class MapUtils {
    /**
     * Safely retrieves a value from a deeply nested structure.
     * Returns null if any key in the path is missing or if an intermediate node is not a Map.
     *
     * @param map The root map.
     * @param keys Sequence of keys to traverse.
     * @return The found value or null.
     */
    static Object getNestedValue(Map map, String... keys) {
        def current = map

        for (String key : keys) {
            if (!(current instanceof Map)) {
                return null
            }
            current = current[key]
        }

        return current
    }

    /**
     * Retrieves a nested value and casts it to a Map. Returns empty map [:] if null.
     */
    static Map getNestedMap(Map map, String... keys) {
        def value = getNestedValue(map, keys)
        return value instanceof Map ? value : [:]
    }

    /**
     * Retrieves a nested value and casts it to a List. Returns empty list [] if null.
     */
    static List getNestedList(Map map, String... keys) {
        def value = getNestedValue(map, keys)
        return value instanceof List ? value : []
    }

    /**
     * Sets a value at a deep path, creating intermediate Maps as needed.
     *
     * @param map The root map to modify.
     * @param path List of keys representing the path.
     * @param value The value to set.
     * @param strict If true, fails if an intermediate node exists but is not a Map.
     * @return true if successful, false otherwise.
     */
    static boolean setNestedValue(
            Map map,
            List path,
            Object value,
            boolean strict = true
    ) {
        if (!map || !path) return false

        def current = map

        for (int i = 0; i < path.size() - 1; i++) {
            String key = path[i]
            def next = current[key]

            if (!(next instanceof Map)) {
                if (strict && next != null) return false
                next = [:]
                current[key] = next
            }
            current = next
        }

        String lastKey = path[-1]
        current[lastKey] = value

        return true
    }

    /**
     * Overload of setNestedValue accepting a dot-notation string path (e.g., "a.b.c").
     */
    static boolean setNestedValue(
            Map map,
            String path,
            Object value,
            boolean strict = true
    ) {
        return setNestedValue(map, path.tokenize('.'), value, strict)
    }

    static boolean hasNestedKey(Map map, String... keys) {
        return getNestedValue(map, keys) != null
    }

    /**
     * Expands a flat map with dot-notation keys into a nested map structure.
     * Example: ['app.name': 'demo'] becomes [app: [name: 'demo']]
     */
    static Map expandFlatMap(Map map) {
        def nested = [:]

        map?.each { k, v ->
            if (k == null) return
            setNestedValue(nested, k.toString(), v, false)
        }

        return nested
    }
}