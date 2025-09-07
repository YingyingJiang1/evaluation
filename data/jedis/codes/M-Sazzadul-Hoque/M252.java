  public byte[] binaryMatch() {
    if (params.containsKey(MATCH)) {
      return params.get(MATCH).array();
    } else {
      return null;
    }
  }
