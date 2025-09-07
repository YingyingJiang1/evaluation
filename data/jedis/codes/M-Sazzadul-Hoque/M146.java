  @Deprecated
  protected final void setProtocol(RedisProtocol protocol) {
    this.protocol = protocol;
    this.commandObjects.setProtocol(this.protocol);
  }
