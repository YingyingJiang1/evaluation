        HashEntry<K, V> newHashEntry(
                K key, int hash, HashEntry<K, V> next, V value) {
            return new HashEntry<K, V>(
                    key, hash, next, value, refQueue);
        }
