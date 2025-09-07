  public static Document load(String id, double score, List<byte[]> fields, boolean decode,
      Map<String, Boolean> isFieldDecode) {
    Document ret = new Document(id, score);
    if (fields != null) {
      for (int i = 0; i < fields.size(); i += 2) {
        byte[] rawKey = fields.get(i);
        byte[] rawValue = fields.get(i + 1);
        String key = SafeEncoder.encode(rawKey);
        Object value = rawValue == null ? null
            : (decode && (isFieldDecode == null || !Boolean.FALSE.equals(isFieldDecode.get(key))))
            ? SafeEncoder.encode(rawValue) : rawValue;
        ret.set(key, value);
      }
    }
    return ret;
  }
