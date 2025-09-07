    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "arthas", name = { "embedded-redis.enabled" })
    public RedisServer embeddedRedisServer(ArthasProperties arthasProperties) {
        EmbeddedRedis embeddedRedis = arthasProperties.getEmbeddedRedis();

        RedisServerBuilder builder = RedisServer.builder().port(embeddedRedis.getPort()).bind(embeddedRedis.getHost());

        for (String setting : embeddedRedis.getSettings()) {
            builder.setting(setting);
        }
        RedisServer redisServer = builder.build();
        return redisServer;
    }
