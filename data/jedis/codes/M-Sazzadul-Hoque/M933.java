  @Deprecated
  public static DefaultJedisClientConfig copyConfig(JedisClientConfig copy) {
    Builder builder = builder();
    builder.protocol(copy.getRedisProtocol());
    builder.connectionTimeoutMillis(copy.getConnectionTimeoutMillis());
    builder.socketTimeoutMillis(copy.getSocketTimeoutMillis());
    builder.blockingSocketTimeoutMillis(copy.getBlockingSocketTimeoutMillis());

    Supplier<RedisCredentials> credentialsProvider = copy.getCredentialsProvider();
    if (credentialsProvider != null) {
      builder.credentialsProvider(credentialsProvider);
    } else {
      builder.user(copy.getUser());
      builder.password(copy.getPassword());
    }

    builder.database(copy.getDatabase());
    builder.clientName(copy.getClientName());

    builder.ssl(copy.isSsl());
    builder.sslSocketFactory(copy.getSslSocketFactory());
    builder.sslParameters(copy.getSslParameters());
    builder.hostnameVerifier(copy.getHostnameVerifier());
    builder.sslOptions(copy.getSslOptions());
    builder.hostAndPortMapper(copy.getHostAndPortMapper());

    builder.clientSetInfoConfig(copy.getClientSetInfoConfig());
    if (copy.isReadOnlyForRedisClusterReplicas()) {
      builder.readOnlyForRedisClusterReplicas();
    }

    builder.authXManager(copy.getAuthXManager());

    return builder.build();
  }
