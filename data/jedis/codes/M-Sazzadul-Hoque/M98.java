    @Override
    @SuppressWarnings("unchecked")
    public List<StreamConsumerInfo> build(Object data) {
      if (null == data) {
        return null;
      }

      List<StreamConsumerInfo> list = new ArrayList<>();
      List<Object> streamsEntries = (List<Object>) data;
      Iterator<Object> groupsArray = streamsEntries.iterator();

      while (groupsArray.hasNext()) {

        List<Object> groupInfo = (List<Object>) groupsArray.next();

        Iterator<Object> consumerInfoIterator = groupInfo.iterator();

        StreamConsumerInfo streamConsumerInfo = new StreamConsumerInfo(
            createMapFromDecodingFunctions(consumerInfoIterator, mappingFunctions));
        list.add(streamConsumerInfo);
      }

      return list;
    }
