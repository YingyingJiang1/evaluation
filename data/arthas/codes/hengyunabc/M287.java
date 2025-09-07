        void clear() {
            if (count != 0) {
                lock();
                try {
                    Arrays.fill(table, null);
                    ++ modCount;
                    // replace the reference queue to avoid unnecessary stale
                    // cleanups
                    refQueue = new ReferenceQueue<Object>();
                    count = 0; // write-volatile
                } finally {
                    unlock();
                }
            }
        }
