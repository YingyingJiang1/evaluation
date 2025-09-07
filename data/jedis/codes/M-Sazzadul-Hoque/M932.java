  @Deprecated
  public static DefaultJedisClientConfig create(int connectionTimeoutMillis, int soTimeoutMillis,
      int blockingSocketTimeoutMillis, String user, String password, int database, String clientName,
      boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, HostAndPortMapper hostAndPortMapper) {
    Builder builder = builder();
    builder.connectionTimeoutMillis(connectionTimeoutMillis).socketTimeoutMillis(soTimeoutMillis)
        .blockingSocketTimeoutMillis(blockingSocketTimeoutMillis);
    if (user != null || password != null) {
      // deliberately not handling 'user != null && password == null' here
      builder.credentials(new DefaultRedisCredentials(user, password));
    }
    builder.database(database).clientName(clientName);
    builder.ssl(ssl).sslSocketFactory(sslSocketFactory).sslParameters(sslParameters).hostnameVerifier(hostnameVerifier);
    builder.hostAndPortMapper(hostAndPortMapper);
    return builder.build();
  }
