    private Map<String, Builder> createDecoderMap() {

      Map<String, Builder> tempMappingFunctions = new HashMap<>();
      tempMappingFunctions.put(StreamGroupFullInfo.NAME, STRING);
      tempMappingFunctions.put(StreamGroupFullInfo.CONSUMERS, STREAM_CONSUMER_FULL_INFO_LIST);
      tempMappingFunctions.put(StreamGroupFullInfo.PENDING, ENCODED_OBJECT_LIST);
      tempMappingFunctions.put(StreamGroupFullInfo.LAST_DELIVERED, STREAM_ENTRY_ID);
      tempMappingFunctions.put(StreamGroupFullInfo.PEL_COUNT, LONG);

      return tempMappingFunctions;
    }
