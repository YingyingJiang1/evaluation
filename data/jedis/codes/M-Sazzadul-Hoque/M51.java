    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> build(Object data) {
      final List<Object> list = (List<Object>) data;
      if (list.isEmpty()) return Collections.emptyMap();

      if (list.get(0) instanceof KeyValue) {
        final Map<String, String> map = new HashMap<>(list.size(), 1f);
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          KeyValue kv = (KeyValue) iterator.next();
          map.put(STRING.build(kv.getKey()), STRING.build(kv.getValue()));
        }
        return map;
      } else {
        final Map<String, String> map = new HashMap<>(list.size() / 2, 1f);
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          map.put(STRING.build(iterator.next()), STRING.build(iterator.next()));
        }
        return map;
      }
    }
