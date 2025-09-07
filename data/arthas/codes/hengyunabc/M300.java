    @Override
    public void clear() {
        for (Segment<K, V> segment: segments) {
            segment.clear();
        }
    }
