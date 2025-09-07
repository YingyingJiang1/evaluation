    @Override
    public ReadCache readCache(PackagePart sharedStringsTablePackagePart) {
        long size = sharedStringsTablePackagePart.getSize();
        if (size < 0) {
            try {
                size = sharedStringsTablePackagePart.getInputStream().available();
            } catch (IOException e) {
                LOGGER.warn("Unable to get file size, default used MapCache");
                return new MapCache();
            }
        }
        if (maxUseMapCacheSize == null) {
            maxUseMapCacheSize = DEFAULT_MAX_USE_MAP_CACHE_SIZE;
        }
        if (size < maxUseMapCacheSize * B2M) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Use map cache.size:{}", size);
            }
            return new MapCache();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Use ehcache.size:{}", size);
        }

        // In order to be compatible with the code
        // If the user set up `maxCacheActivateSize`, then continue using it
        if (maxCacheActivateSize != null) {
            return new Ehcache(maxCacheActivateSize, maxCacheActivateBatchCount);
        } else {
            if (maxCacheActivateBatchCount == null) {
                maxCacheActivateBatchCount = DEFAULT_MAX_EHCACHE_ACTIVATE_BATCH_COUNT;
            }
            return new Ehcache(maxCacheActivateSize, maxCacheActivateBatchCount);
        }

    }
