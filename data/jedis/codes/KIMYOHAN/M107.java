    @Override
    @SuppressWarnings("unchecked")
    public List<Map.Entry<byte[], List<StreamEntryBinary>>> build(Object data) {
      if (data == null) return null;
      List list = (List) data;
      if (list.isEmpty()) return Collections.emptyList();

      if (list.get(0) instanceof KeyValue) {
        return ((List<KeyValue>) list).stream()
                .map(kv -> new KeyValue<>(BINARY.build(kv.getKey()),
                    STREAM_ENTRY_BINARY_LIST.build(kv.getValue())))
                .collect(Collectors.toList());
      } else {
        List<Map.Entry<byte[], List<StreamEntryBinary>>> result = new ArrayList<>(list.size());
        for (Object anObj : list) {
          List<Object> streamObj = (List<Object>) anObj;
          byte[] streamKey = BINARY.build(streamObj.get(0));
          List<StreamEntryBinary> streamEntries = STREAM_ENTRY_BINARY_LIST.build(streamObj.get(1));
          result.add(KeyValue.of(streamKey, streamEntries));
        }
        return result;
      }
    }
