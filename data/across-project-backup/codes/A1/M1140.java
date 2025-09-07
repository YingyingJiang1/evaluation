  @Override
  public String discard() {
    if (!inMulti) {
      throw new IllegalStateException("DISCARD without MULTI");
    }

    try {
      // ignore QUEUED (or ERROR)
      // processPipelinedResponses(pipelinedResponses.size());
      connection.getMany(1 + pipelinedResponses.size());

      connection.sendCommand(DISCARD);

      return connection.getStatusCodeReply();
    } catch (JedisConnectionException jce) {
      broken = true;
      throw jce;
    } finally {
      inMulti = false;
      inWatch = false;
      pipelinedResponses.clear();
      if (jedis != null) {
        jedis.resetState();
      }
    }
  }
