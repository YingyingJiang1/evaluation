    private static Constructor findConstructorWithCacheable(Class customCacheType) {
        return Arrays.stream(customCacheType.getConstructors())
                .filter(ctor -> Arrays.equals(ctor.getParameterTypes(), new Class[] { int.class, EvictionPolicy.class, Cacheable.class }))
                .findFirst().orElse(null);
    }
