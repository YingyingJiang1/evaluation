  @Override
  public void addParams(CommandArguments args) {

    if (type != null) {
      args.add(SearchKeyword.ON.name());
      args.add(type.name());
    }

    if (prefixes != null && prefixes.length > 0) {
      args.add(SearchKeyword.PREFIX.name());
      args.add(Integer.toString(prefixes.length));
      args.addObjects((Object[]) prefixes);
    }

    if (filter != null) {
      args.add(SearchKeyword.FILTER.name());
      args.add(filter);
    }

    if (languageField != null) {
      args.add(SearchKeyword.LANGUAGE_FIELD.name());
      args.add(languageField);
    }

    if (language != null) {
      args.add(SearchKeyword.LANGUAGE.name());
      args.add(language);
    }

    if (scoreFiled != null) {
      args.add(SearchKeyword.SCORE_FIELD.name());
      args.add(scoreFiled);
    }

    if (score != 1.0) {
      args.add(SearchKeyword.SCORE.name());
      args.add(Double.toString(score));
    }
  }
