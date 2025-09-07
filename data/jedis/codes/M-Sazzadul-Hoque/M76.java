    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> build(Object data) {
      List<Object> result = (List<Object>) data;
      byte[] newcursor = (byte[]) result.get(0);
      List<byte[]> rawResults = (List<byte[]>) result.get(1);
      List<Map.Entry<byte[], byte[]>> results = new ArrayList<>(rawResults.size() / 2);
      Iterator<byte[]> iterator = rawResults.iterator();
      while (iterator.hasNext()) {
        results.add(new AbstractMap.SimpleEntry<>(iterator.next(), iterator.next()));
      }
      return new ScanResult<>(newcursor, results);
    }
