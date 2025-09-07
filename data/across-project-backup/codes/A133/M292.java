    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        // See explanation of modCount use above

        final Segment<K, V>[] segments = this.segments;
        int[] mc = new int[segments.length];

        // Try a few times without locking
        for (int k = 0; k < RETRIES_BEFORE_LOCK; ++ k) {
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++ i) {
                mcsum += mc[i] = segments[i].modCount;
                if (segments[i].containsValue(value)) {
                    return true;
                }
            }
            boolean cleanSweep = true;
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++ i) {
                    if (mc[i] != segments[i].modCount) {
                        cleanSweep = false;
                        break;
                    }
                }
            }
            if (cleanSweep) {
                return false;
            }
        }
        // Resort to locking all segments
        for (Segment<K, V> segment: segments) {
            segment.lock();
        }
        boolean found = false;
        try {
            for (Segment<K, V> segment: segments) {
                if (segment.containsValue(value)) {
                    found = true;
                    break;
                }
            }
        } finally {
            for (Segment<K, V> segment: segments) {
                segment.unlock();
            }
        }
        return found;
    }
