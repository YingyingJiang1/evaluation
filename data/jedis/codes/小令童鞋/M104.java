    @Override
    @SuppressWarnings("unchecked")
    public StreamFullInfo build(Object data) {
      if (null == data) {
        return null;
      }

      List<Object> streamsEntries = (List<Object>) data;
      Iterator<Object> iterator = streamsEntries.iterator();

      return new StreamFullInfo(createMapFromDecodingFunctions(iterator, mappingFunctions));
    }
