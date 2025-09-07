    @Override
    public Object build(Object data) {
      if (data == null) return null;

      if (data instanceof List) {
        final List list = (List) data;
        if (list.isEmpty()) {
          return list == Protocol.PROTOCOL_EMPTY_MAP ? Collections.emptyMap() : Collections.emptyList();
        }

        if (list.get(0) instanceof KeyValue) {
          return ((List<KeyValue>) data).stream()
              .filter(kv -> kv != null && kv.getKey() != null && kv.getValue() != null)
              .collect(Collectors.toMap(kv -> STRING.build(kv.getKey()),
                  kv -> this.build(kv.getValue())));
        } else {
          return list.stream().map(this::build).collect(Collectors.toList());
        }
      } else if (data instanceof byte[]) {
        return STRING.build(data);
      } else {
        return data;
      }
    }
