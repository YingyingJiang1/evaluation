    public void purgeStaleEntries() {
        for (Segment<K, V> segment: segments) {
            segment.removeStale();
        }
    }
