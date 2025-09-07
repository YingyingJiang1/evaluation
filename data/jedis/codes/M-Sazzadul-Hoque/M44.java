  @Override
  public String discard() {
    if (!inMulti) {
      throw new IllegalStateException("DISCARD without MULTI");
    }

    try {
      // processPipelinedResponses(pipelinedResponses.size());
      // do nothing
      connection.sendCommand(DISCARD);
      String status = connection.getStatusCodeReply();
      if (!"OK".equals(status)) {
        throw new JedisException("DISCARD command failed. Received response: " + status);
      }
      return status;
    } catch (JedisConnectionException jce) {
      broken = true;
      throw jce;
    } finally {
      inMulti = false;
      inWatch = false;
      pipelinedResponses.clear();
    }
  }
