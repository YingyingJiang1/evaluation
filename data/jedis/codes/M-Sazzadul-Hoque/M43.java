  @Override
  public List<Object> exec() {
    if (!inMulti) {
      throw new IllegalStateException("EXEC without MULTI");
    }

    try {
      // processPipelinedResponses(pipelinedResponses.size());
      // do nothing
      connection.sendCommand(EXEC);

      List<Object> unformatted = connection.getObjectMultiBulkReply();
      if (unformatted == null) {
        pipelinedResponses.clear();
        return null;
      }

      List<Object> formatted = new ArrayList<>(unformatted.size());
      for (Object o : unformatted) {
        try {
          Response<?> response = pipelinedResponses.poll();
          response.set(o);
          formatted.add(response.get());
        } catch (JedisDataException e) {
          formatted.add(e);
        }
      }
      return formatted;
    } catch (JedisConnectionException jce) {
      broken = true;
      throw jce;
    } finally {
      inMulti = false;
      inWatch = false;
      pipelinedResponses.clear();
    }
  }
