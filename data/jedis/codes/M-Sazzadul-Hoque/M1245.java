  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(TAG);

    if (indexMissing) {
      args.add(INDEXMISSING);
    }
    if (indexEmpty) {
      args.add(INDEXEMPTY);
    }

    if (separator != null) {
      args.add(SEPARATOR).add(separator);
    }

    if (caseSensitive) {
      args.add(CASESENSITIVE);
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
