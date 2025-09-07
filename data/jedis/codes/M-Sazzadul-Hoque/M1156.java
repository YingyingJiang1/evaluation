    @Override
    public Map<String, CommandInfo> build(Object data) {
      if (data == null) {
        return null;
      }

      List<Object> rawList = (List<Object>) data;
      Map<String, CommandInfo> map = new HashMap<>(rawList.size());

      for (Object rawCommandInfo : rawList) {
        CommandInfo info = CommandInfo.COMMAND_INFO_BUILDER.build(rawCommandInfo);
        if (info != null) {
          map.put(info.getName(), info);
        }
      }

      return map;
    }
