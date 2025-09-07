    @Override
    public List<Class<?>> build(Object data) {
      List<List<Class<?>>> fullReply = JSON_TYPE_RESPONSE_RESP3.build(data);
      return fullReply == null ? null : fullReply.get(0);
    }
