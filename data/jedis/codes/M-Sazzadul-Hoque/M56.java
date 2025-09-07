    @Override
    @SuppressWarnings("unchecked")
    public KeyValue<String, String> build(Object data) {
      if (data == null) return null;
      List<Object> l = (List<Object>) data;
      return KeyValue.of(STRING.build(l.get(0)), STRING.build(l.get(1)));
    }
