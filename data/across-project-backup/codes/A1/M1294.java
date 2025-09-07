  @Override
  public void addParams(CommandArguments args) {

    if (definition != null) {
      definition.addParams(args);
    }

    if ((flags & USE_TERM_OFFSETS) == 0) {
      args.add(SearchKeyword.NOOFFSETS.name());
    }
    if ((flags & KEEP_FIELD_FLAGS) == 0) {
      args.add(SearchKeyword.NOFIELDS.name());
    }
    if ((flags & KEEP_TERM_FREQUENCIES) == 0) {
      args.add(SearchKeyword.NOFREQS.name());
    }
    if (expire > 0) {
      args.add(SearchKeyword.TEMPORARY.name());
      args.add(Long.toString(this.expire));
    }

    if (stopwords != null) {
      args.add(SearchKeyword.STOPWORDS.name());
      args.add(Integer.toString(stopwords.size()));
      if (!stopwords.isEmpty()) {
        args.addObjects(stopwords);
      }
    }
  }
