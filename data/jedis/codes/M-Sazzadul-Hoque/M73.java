    @Override
    public ScanResult<Map.Entry<String, String>> build(Object data) {
      List<Object> result = (List<Object>) data;
      String newcursor = new String((byte[]) result.get(0));
      List<byte[]> rawResults = (List<byte[]>) result.get(1);
      List<Map.Entry<String, String>> results = new ArrayList<>(rawResults.size() / 2);
      Iterator<byte[]> iterator = rawResults.iterator();
      while (iterator.hasNext()) {
        results.add(new AbstractMap.SimpleEntry<>(SafeEncoder.encode(iterator.next()),
            SafeEncoder.encode(iterator.next())));
      }
      return new ScanResult<>(newcursor, results);
    }
