    private static MemoryUsage getUsage(MemoryPoolMXBean memoryPoolMXBean) {
        try {
            return memoryPoolMXBean.getUsage();
        } catch (InternalError e) {
            // Defensive for potential InternalError with some specific JVM options. Based on its Javadoc,
            // MemoryPoolMXBean.getUsage() should return null, not throwing InternalError, so it seems to be a JVM bug.
            return null;
        }
    }
