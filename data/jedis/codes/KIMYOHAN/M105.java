    @Override
    @SuppressWarnings("unchecked")
    public List<StreamEntryBinary> build(Object data) {
      if (null == data) {
        return null;
      }
      List<ArrayList<Object>> objectList = (List<ArrayList<Object>>) data;

      List<StreamEntryBinary> responses = new ArrayList<>(objectList.size() / 2);
      if (objectList.isEmpty()) {
        return responses;
      }

      for (ArrayList<Object> res : objectList) {
        if (res == null) {
          responses.add(null);
          continue;
        }
        String entryIdString = SafeEncoder.encode((byte[]) res.get(0));
        StreamEntryID entryID = new StreamEntryID(entryIdString);
        List<byte[]> hash = (List<byte[]>) res.get(1);
        if (hash == null) {
          responses.add(new StreamEntryBinary(entryID, null));
          continue;
        }

        Iterator<byte[]> hashIterator = hash.iterator();
        Map<byte[], byte[]> map = new JedisByteHashMap();
        while (hashIterator.hasNext()) {
          map.put(BINARY.build(hashIterator.next()), BINARY.build(hashIterator.next()));
        }
        responses.add(new StreamEntryBinary(entryID, map));
      }

      return responses;
    }
