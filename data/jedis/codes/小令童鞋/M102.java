    @Override
    @SuppressWarnings("unchecked")
    public List<StreamGroupFullInfo> build(Object data) {
      if (null == data) {
        return null;
      }

      List<StreamGroupFullInfo> list = new ArrayList<>();
      List<Object> streamsEntries = (List<Object>) data;

      for (Object streamsEntry : streamsEntries) {

        List<Object> groupInfo = (List<Object>) streamsEntry;

        Iterator<Object> groupInfoIterator = groupInfo.iterator();

        StreamGroupFullInfo groupFullInfo = new StreamGroupFullInfo(
            createMapFromDecodingFunctions(groupInfoIterator, mappingFunctions));
        list.add(groupFullInfo);

      }
      return list;
    }
