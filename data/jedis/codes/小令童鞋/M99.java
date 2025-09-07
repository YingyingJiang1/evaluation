    private Map<String, Builder> createDecoderMap() {

      Map<String, Builder> tempMappingFunctions = new HashMap<>();
      tempMappingFunctions.put(StreamConsumerFullInfo.NAME, STRING);
      tempMappingFunctions.put(StreamConsumerFullInfo.SEEN_TIME, LONG);
      tempMappingFunctions.put(StreamConsumerFullInfo.PEL_COUNT, LONG);
      tempMappingFunctions.put(StreamConsumerFullInfo.PENDING, ENCODED_OBJECT_LIST);

      return tempMappingFunctions;
    }
