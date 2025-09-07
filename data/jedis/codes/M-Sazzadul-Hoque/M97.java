    private Map<String, Builder> createDecoderMap() {
      Map<String, Builder> tempMappingFunctions = new HashMap<>();
      tempMappingFunctions.put(StreamConsumerInfo.NAME, STRING);
      tempMappingFunctions.put(StreamConsumerInfo.IDLE, LONG);
      tempMappingFunctions.put(StreamConsumerInfo.PENDING, LONG);
      return tempMappingFunctions;

    }
