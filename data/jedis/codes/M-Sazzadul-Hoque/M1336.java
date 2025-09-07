  private void addReturnFieldDecode(String returnName, boolean decode) {
    if (returnFieldDecodeMap == null) {
      returnFieldDecodeMap = new HashMap<>();
    }
    returnFieldDecodeMap.put(returnName, decode);
  }
