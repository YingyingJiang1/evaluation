    @Override
    public List<Map.Entry<String, List<StreamEntry>>> build(Object data) {
      if (data == null) return null;
      List list = (List) data;
      if (list.isEmpty()) return Collections.emptyList();

      if (list.get(0) instanceof KeyValue) {
        return ((List<KeyValue>) list).stream()
            .map(kv -> new KeyValue<>(STRING.build(kv.getKey()),
                STREAM_ENTRY_LIST.build(kv.getValue())))
            .collect(Collectors.toList());
      } else {
        List<Map.Entry<String, List<StreamEntry>>> result = new ArrayList<>(list.size());
        for (Object anObj : list) {
          List<Object> streamObj = (List<Object>) anObj;
          String streamKey = STRING.build(streamObj.get(0));
          List<StreamEntry> streamEntries = STREAM_ENTRY_LIST.build(streamObj.get(1));
          result.add(KeyValue.of(streamKey, streamEntries));
        }
        return result;
      }
    }
