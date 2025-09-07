    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine")
    public TunnelClusterStore tunnelClusterStore(@Autowired CacheManager cacheManager) {
        Cache inMemoryClusterCache = cacheManager.getCache("inMemoryClusterCache");
        InMemoryClusterStore inMemoryClusterStore = new InMemoryClusterStore();
        inMemoryClusterStore.setCache(inMemoryClusterCache);
        return inMemoryClusterStore;
    }
