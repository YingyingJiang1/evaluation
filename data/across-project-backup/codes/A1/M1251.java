  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(TEXT);

    if (indexMissing) {
      args.add(INDEXMISSING);
    }
    if (indexEmpty) {
      args.add(INDEXEMPTY);
    }

    if (weight != null) {
      args.add(WEIGHT).add(weight);
    }

    if (noStem) {
      args.add(NOSTEM);
    }

    if (phoneticMatcher != null) {
      args.add(PHONETIC).add(phoneticMatcher);
    }

    if (withSuffixTrie) {
      args.add(WITHSUFFIXTRIE);
    }

    if (sortableUNF) {
      args.add(SORTABLE).add(UNF);
    } else if (sortable) {
      args.add(SORTABLE);
    }

    if (noIndex) {
      args.add(NOINDEX);
    }
  }
