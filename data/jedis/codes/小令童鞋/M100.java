    @Override
    @SuppressWarnings("unchecked")
    public List<StreamConsumerFullInfo> build(Object data) {
      if (null == data) {
        return null;
      }

      List<StreamConsumerFullInfo> list = new ArrayList<>();
      List<Object> streamsEntries = (List<Object>) data;

      for (Object streamsEntry : streamsEntries) {
        List<Object> consumerInfoList = (List<Object>) streamsEntry;
        Iterator<Object> consumerInfoIterator = consumerInfoList.iterator();
        StreamConsumerFullInfo consumerInfo = new StreamConsumerFullInfo(
            createMapFromDecodingFunctions(consumerInfoIterator, mappingFunctions));
        list.add(consumerInfo);
      }
      return list;
    }
