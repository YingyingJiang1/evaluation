        @Bean
        // @ConditionalOnBean(StringRedisTemplate.class)
        @ConditionalOnClass(StringRedisTemplate.class)
        @ConditionalOnProperty("spring.redis.host")
        @ConditionalOnMissingBean
        public TunnelClusterStore tunnelClusterStore(@Autowired StringRedisTemplate redisTemplate) {
            RedisTunnelClusterStore redisTunnelClusterStore = new RedisTunnelClusterStore();
            redisTunnelClusterStore.setRedisTemplate(redisTemplate);
            return redisTunnelClusterStore;
        }
