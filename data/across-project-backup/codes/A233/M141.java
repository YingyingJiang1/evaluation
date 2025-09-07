    public StreamCacheCreateFunction getStreamCacheFunction(long contentSize) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;

        // Calculate percentage of free memory
        double freeMemoryPercent = (double) (maxMemory - usedMemory) / maxMemory * 100;
        long actualFreeMemory = maxMemory - usedMemory;

        // Log memory status
        log.debug(
                "Memory status - Free: {}MB ({}%), Used: {}MB, Max: {}MB",
                actualFreeMemory / (1024 * 1024),
                String.format("%.2f", freeMemoryPercent),
                usedMemory / (1024 * 1024),
                maxMemory / (1024 * 1024));

        // If free memory is critically low, always use file-based caching
        if (freeMemoryPercent < MIN_FREE_MEMORY_PERCENTAGE
                || actualFreeMemory < MIN_FREE_MEMORY_BYTES) {
            log.debug(
                    "Low memory detected ({}%), forcing file-based cache",
                    String.format("%.2f", freeMemoryPercent));
            return createScratchFileCacheFunction(MemoryUsageSetting.setupTempFileOnly());
        } else if (contentSize < SMALL_FILE_THRESHOLD) {
            log.debug("Using memory-only cache for small document ({}KB)", contentSize / 1024);
            return IOUtils.createMemoryOnlyStreamCache();
        } else if (contentSize < LARGE_FILE_THRESHOLD) {
            // For medium files (10-50MB), use a mixed approach
            log.debug(
                    "Using mixed memory/file cache for medium document ({}MB)",
                    contentSize / (1024 * 1024));
            return createScratchFileCacheFunction(MemoryUsageSetting.setupMixed(LARGE_FILE_USAGE));
        } else {
            log.debug("Using file-based cache for large document");
            return createScratchFileCacheFunction(MemoryUsageSetting.setupTempFileOnly());
        }
    }
