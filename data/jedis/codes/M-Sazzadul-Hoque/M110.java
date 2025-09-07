    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> build(Object data) {
      final List list = (List) data;
      if (list.isEmpty()) return Collections.emptyMap();

      if (list.get(0) instanceof KeyValue) {
        return ((List<KeyValue>) list).stream()
            .collect(Collectors.toMap(kv -> STRING.build(kv.getKey()),
                kv -> ENCODED_OBJECT.build(kv.getValue())));
      }

      final Map<String, Object> map = new HashMap<>(list.size());
      for (Object object : list) {
        if (object == null) continue;
        final List<Object> flat = (List<Object>) object;
        if (flat.isEmpty()) continue;
        map.put(STRING.build(flat.get(0)), STRING.build(flat.get(1)));
      }
      return map;
    }
