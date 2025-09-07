    @Override
    public List<AccessControlLogEntry> build(Object data) {

      if (null == data) {
        return null;
      }

      List<AccessControlLogEntry> list = new ArrayList<>();
      List<List<Object>> logEntries = (List<List<Object>>) data;
      for (List<Object> logEntryData : logEntries) {
        Iterator<Object> logEntryDataIterator = logEntryData.iterator();
        AccessControlLogEntry accessControlLogEntry = new AccessControlLogEntry(
            createMapFromDecodingFunctions(logEntryDataIterator, mappingFunctions,
                BACKUP_BUILDERS_FOR_DECODING_FUNCTIONS));
        list.add(accessControlLogEntry);
      }
      return list;
    }
