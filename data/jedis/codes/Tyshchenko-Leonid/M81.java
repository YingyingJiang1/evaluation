    @Override
    public List<LatencyHistoryInfo> build(Object data) {
      if (data == null) {
        return null;
      }

      List<Object> rawList = (List<Object>) data;
      List<LatencyHistoryInfo> response = new ArrayList<>(rawList.size());

      for (Object rawLatencyHistoryInfo : rawList) {
        if (rawLatencyHistoryInfo == null) {
          continue;
        }

        LatencyHistoryInfo historyInfo = LatencyHistoryInfo.LATENCY_HISTORY_BUILDER.build(rawLatencyHistoryInfo);
        response.add(historyInfo);
      }

      return response;
    }
