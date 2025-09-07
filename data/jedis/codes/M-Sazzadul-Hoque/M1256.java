  public Query limit(Integer offset, Integer limit) {
    _paging.offset = offset;
    _paging.num = limit;
    return this;
  }
