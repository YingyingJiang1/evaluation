    private Map<String, Builder> createDecoderMap() {

      Map<String, Builder> tempMappingFunctions = new HashMap<>();
      tempMappingFunctions.put(StreamFullInfo.LAST_GENERATED_ID, STREAM_ENTRY_ID);
      tempMappingFunctions.put(StreamFullInfo.LENGTH, LONG);
      tempMappingFunctions.put(StreamFullInfo.RADIX_TREE_KEYS, LONG);
      tempMappingFunctions.put(StreamFullInfo.RADIX_TREE_NODES, LONG);
      tempMappingFunctions.put(StreamFullInfo.GROUPS, STREAM_GROUP_FULL_INFO_LIST);
      tempMappingFunctions.put(StreamFullInfo.ENTRIES, STREAM_ENTRY_LIST);

      return tempMappingFunctions;
    }
