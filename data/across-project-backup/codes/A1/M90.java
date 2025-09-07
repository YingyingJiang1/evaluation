    @Override
    @SuppressWarnings("unchecked")
    public StreamEntry build(Object data) {
      if (null == data) {
        return null;
      }
      List<Object> objectList = (List<Object>) data;

      if (objectList.isEmpty()) {
        return null;
      }

      String entryIdString = SafeEncoder.encode((byte[]) objectList.get(0));
      StreamEntryID entryID = new StreamEntryID(entryIdString);
      List<byte[]> hash = (List<byte[]>) objectList.get(1);

      Iterator<byte[]> hashIterator = hash.iterator();
      Map<String, String> map = new HashMap<>(hash.size() / 2, 1f);
      while (hashIterator.hasNext()) {
        map.put(SafeEncoder.encode(hashIterator.next()), SafeEncoder.encode(hashIterator.next()));
      }
      return new StreamEntry(entryID, map);
    }
