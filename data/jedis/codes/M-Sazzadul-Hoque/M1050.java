  public final CommandObject<Map<String, TSMGetElement>> tsMGet(TSMGetParams multiGetParams, String... filters) {
    return new CommandObject<>(commandArguments(TimeSeriesCommand.MGET).addParams(multiGetParams)
        .add(TimeSeriesKeyword.FILTER).addObjects((Object[]) filters),
        protocol == RedisProtocol.RESP3 ? TimeSeriesBuilderFactory.TIMESERIES_MGET_RESPONSE_RESP3
            : TimeSeriesBuilderFactory.TIMESERIES_MGET_RESPONSE);
  }
