  void setHostAndPort(final HostAndPort hostAndPort) {
    if (!(jedisSocketFactory instanceof DefaultJedisSocketFactory)) {
      throw new IllegalStateException("setHostAndPort method has limited capability.");
    }
    ((DefaultJedisSocketFactory) jedisSocketFactory).updateHostAndPort(hostAndPort);
  }
