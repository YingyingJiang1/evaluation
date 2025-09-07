    @Override
    @SuppressWarnings("unchecked")
    public Map<byte[], List<StreamEntryBinary>> build(Object data) {
      if (data == null) return null;
      List list = (List) data;
      if (list.isEmpty()) return Collections.emptyMap();

      JedisByteMap<List<StreamEntryBinary>> result = new JedisByteMap<>();
      if (list.get(0) instanceof KeyValue) {
        ((List<KeyValue>) list).forEach(kv -> result.put(BINARY.build(kv.getKey()), STREAM_ENTRY_BINARY_LIST.build(kv.getValue())));
        return result;
      } else {
        for (Object anObj : list) {
          List<Object> streamObj = (List<Object>) anObj;
          byte[] streamKey = (byte[]) streamObj.get(0);
          List<StreamEntryBinary> streamEntries = STREAM_ENTRY_BINARY_LIST.build(streamObj.get(1));
          result.put(streamKey, streamEntries);
        }
        return result;
      }
    }
