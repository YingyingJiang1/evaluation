    private static Constructor getConstructor(Class customCacheType) {
        try {
            return customCacheType.getConstructor(int.class, EvictionPolicy.class);
        } catch (NoSuchMethodException e) {
            String className = customCacheType.getName();
            throw new JedisCacheException(String.format(
                "Failed to find compatible constructor for custom cache type!  Provide one of these;"
                        // give hints about the compatible constructors
                        + "\n - %s(int maxSize, EvictionPolicy evictionPolicy)\n - %s(int maxSize, EvictionPolicy evictionPolicy, Cacheable cacheable)",
                className, className), e);
        }
    }
