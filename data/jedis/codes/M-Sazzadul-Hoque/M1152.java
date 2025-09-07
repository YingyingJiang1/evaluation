  private Map<String, String> getMapFromRawClientInfo(String clientInfo) {
    String[] entries = clientInfo.split(" ");
    Map<String, String> clientInfoMap = new LinkedHashMap<>(entries.length);
    for (String entry : entries) {
      String[] kvArray = entry.split("=");
      clientInfoMap.put(kvArray[0], (kvArray.length == 2) ? kvArray[1] : "");
    }
    return clientInfoMap;
  }
