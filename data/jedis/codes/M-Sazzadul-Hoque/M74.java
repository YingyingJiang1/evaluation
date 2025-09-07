    @Override
    public ScanResult<String> build(Object data) {
      List<Object> result = (List<Object>) data;
      String newcursor = new String((byte[]) result.get(0));
      List<byte[]> rawResults = (List<byte[]>) result.get(1);
      List<String> results = new ArrayList<>(rawResults.size());
      for (byte[] bs : rawResults) {
        results.add(SafeEncoder.encode(bs));
      }
      return new ScanResult<>(newcursor, results);
    }
