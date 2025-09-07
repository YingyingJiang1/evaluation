    @Override
    public ScanResult<byte[]> build(Object data) {
      List<Object> result = (List<Object>) data;
      byte[] newcursor = (byte[]) result.get(0);
      List<byte[]> rawResults = (List<byte[]>) result.get(1);
      return new ScanResult<>(newcursor, rawResults);
    }
