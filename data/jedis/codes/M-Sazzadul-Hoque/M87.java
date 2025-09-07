    private Map<String, Builder> createDecoderMap() {

      Map<String, Builder> tempMappingFunctions = new HashMap<>();
      tempMappingFunctions.put(AccessControlLogEntry.COUNT, LONG);
      tempMappingFunctions.put(AccessControlLogEntry.REASON, STRING);
      tempMappingFunctions.put(AccessControlLogEntry.CONTEXT, STRING);
      tempMappingFunctions.put(AccessControlLogEntry.OBJECT, STRING);
      tempMappingFunctions.put(AccessControlLogEntry.USERNAME, STRING);
      tempMappingFunctions.put(AccessControlLogEntry.AGE_SECONDS, DOUBLE);
      tempMappingFunctions.put(AccessControlLogEntry.CLIENT_INFO, STRING);
      tempMappingFunctions.put(AccessControlLogEntry.ENTRY_ID, LONG);
      tempMappingFunctions.put(AccessControlLogEntry.TIMESTAMP_CREATED, LONG);
      tempMappingFunctions.put(AccessControlLogEntry.TIMESTAMP_LAST_UPDATED, LONG);

      return tempMappingFunctions;
    }
