    public Builder from(JedisClientConfig instance) {
      this.redisProtocol = instance.getRedisProtocol();
      this.connectionTimeoutMillis = instance.getConnectionTimeoutMillis();
      this.socketTimeoutMillis = instance.getSocketTimeoutMillis();
      this.blockingSocketTimeoutMillis = instance.getBlockingSocketTimeoutMillis();
      this.credentialsProvider = instance.getCredentialsProvider();
      this.database = instance.getDatabase();
      this.clientName = instance.getClientName();
      this.ssl = instance.isSsl();
      this.sslSocketFactory = instance.getSslSocketFactory();
      this.sslParameters = instance.getSslParameters();
      this.sslOptions = instance.getSslOptions();
      this.hostnameVerifier = instance.getHostnameVerifier();
      this.hostAndPortMapper = instance.getHostAndPortMapper();
      this.clientSetInfoConfig = instance.getClientSetInfoConfig();
      this.readOnlyForRedisClusterReplicas = instance.isReadOnlyForRedisClusterReplicas();
      this.authXManager = instance.getAuthXManager();
      return this;
    }
