  private Builder<SearchResult> getSearchResultBuilder(
      Map<String, Boolean> isReturnFieldDecode, Supplier<Builder<SearchResult>> resp2) {
    if (protocol == RedisProtocol.RESP3) {
      return isReturnFieldDecode == null ? SearchResult.SEARCH_RESULT_BUILDER
          : new SearchResult.PerFieldDecoderSearchResultBuilder(isReturnFieldDecode);
    }
    return resp2.get();
  }
