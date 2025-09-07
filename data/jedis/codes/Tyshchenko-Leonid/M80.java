    @Override
    public Map<String, LatencyLatestInfo> build(Object data) {
      if (data == null) {
        return null;
      }

      List<Object> rawList = (List<Object>) data;
      Map<String, LatencyLatestInfo> map = new HashMap<>(rawList.size());

      for (Object rawLatencyLatestInfo : rawList) {
        if (rawLatencyLatestInfo == null) {
          continue;
        }

        LatencyLatestInfo latestInfo = LatencyLatestInfo.LATENCY_LATEST_BUILDER.build(rawLatencyLatestInfo);
        String name = latestInfo.getCommand();
        map.put(name, latestInfo);
      }

      return map;
    }
