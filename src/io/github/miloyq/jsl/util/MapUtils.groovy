package io.github.miloyq.jsl.util

class MapUtils {
    static Object getNestedValue(Map map, String... keys) {
        def current = map

        for (String key : keys) {
            if (!(current instanceof Map)) {
                return null
            }
            current = current[key]
        }

        current
    }

    static Map getNestedMap(Map map, String... keys) {
        def value = getNestedValue(map, keys)
        value instanceof Map ? value : [:]
    }

    static List getNestedList(Map map, String... keys) {
        def value = getNestedValue(map, keys)
        value instanceof List ? value : []
    }

    static boolean setNestedValue(
            Map map,
            List path,
            Object value,
            boolean strict = true
    ) {
        if (!map || !path) return false

        def current = map

        for (int i = 0; i < path.size() - 1; i++) {
            def key = path[i]
            def next = current.get(key)

            if (!(next instanceof Map)) {
                if (strict) return false
                next = [:]
                current.put(key, next)
            }

            current = next
        }

        def lastKey = path[-1]
        if (strict && !current.containsKey(lastKey)) {
            return false
        }

        current.put(lastKey, value)
        true
    }

    static boolean setNestedValue(
            Map map,
            String path,
            Object value,
            boolean strict = true
    ) {
        setNestedValue(map, path.tokenize('.'), value, strict)
    }

    static boolean hasNestedKey(Map map, String... keys) {
        getNestedValue(map, keys) != null
    }

    static Map expandFlatMap(Map map) {
        def nested = [:]

        map?.each { k, v ->
            if (k == null) return
            setNestedValue(nested, k.toString(), v, false)
        }

        nested
    }
}