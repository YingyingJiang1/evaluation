  private static List<KeyValue> processMapKeyValueReply(final RedisInputStream is) {
    final int num = is.readIntCrLf();
    switch (num) {
      case -1:
        return null;
      case 0:
        return PROTOCOL_EMPTY_MAP;
      default:
        final List<KeyValue> ret = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
          ret.add(new KeyValue(process(is), process(is)));
        }
        return ret;
    }
  }
