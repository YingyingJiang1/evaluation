    @Override
    public Map<String, List<StreamEntry>> build(Object data) {
      if (data == null) return null;
      List list = (List) data;
      if (list.isEmpty()) return Collections.emptyMap();

      if (list.get(0) instanceof KeyValue) {
        return ((List<KeyValue>) list).stream()
            .collect(Collectors.toMap(kv -> STRING.build(kv.getKey()), kv -> STREAM_ENTRY_LIST.build(kv.getValue())));
      } else {
        Map<String, List<StreamEntry>> result = new HashMap<>(list.size());
        for (Object anObj : list) {
          List<Object> streamObj = (List<Object>) anObj;
          String streamKey = STRING.build(streamObj.get(0));
          List<StreamEntry> streamEntries = STREAM_ENTRY_LIST.build(streamObj.get(1));
          result.put(streamKey, streamEntries);
        }
        return result;
      }
    }
