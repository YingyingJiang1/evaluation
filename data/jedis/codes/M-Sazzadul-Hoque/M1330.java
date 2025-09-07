  @Override
  public void addParams(CommandArguments args) {

    if (noContent) {
      args.add(NOCONTENT);
    }
    if (verbatim) {
      args.add(VERBATIM);
    }
    if (noStopwords) {
      args.add(NOSTOPWORDS);
    }
    if (withScores) {
      args.add(WITHSCORES);
    }

    if (!filters.isEmpty()) {
      filters.forEach(filter -> filter.addParams(args));
    }

    if (inKeys != null && !inKeys.isEmpty()) {
      args.add(INKEYS).add(inKeys.size()).addObjects(inKeys);
    }

    if (inFields != null && !inFields.isEmpty()) {
      args.add(INFIELDS).add(inFields.size()).addObjects(inFields);
    }

    if (returnFieldsNames != null && !returnFieldsNames.isEmpty()) {
      args.add(RETURN);
      LazyRawable returnCountObject = new LazyRawable();
      args.add(returnCountObject); // holding a place for setting the total count later.
      int returnCount = 0;
      for (FieldName fn : returnFieldsNames) {
        returnCount += fn.addCommandArguments(args);
      }
      returnCountObject.setRaw(Protocol.toByteArray(returnCount));
    }

    if (summarizeParams != null) {
      args.addParams(summarizeParams);
    } else if (summarize) {
      args.add(SUMMARIZE);
    }

    if (highlightParams != null) {
      args.addParams(highlightParams);
    } else if (highlight) {
      args.add(HIGHLIGHT);
    }

    if (slop != null) {
      args.add(SLOP).add(slop);
    }

    if (timeout != null) {
      args.add(TIMEOUT).add(timeout);
    }

    if (inOrder) {
      args.add(INORDER);
    }

    if (language != null) {
      args.add(LANGUAGE).add(language);
    }

    if (expander != null) {
      args.add(EXPANDER).add(expander);
    }

    if (scorer != null) {
      args.add(SCORER).add(scorer);
    }
//
//    if (explainScore) {
//      args.add(EXPLAINSCORE);
//    }

    if (sortBy != null) {
      args.add(SORTBY).add(sortBy);
      if (sortOrder != null) {
        args.add(sortOrder);
      }
    }

    if (limit != null) {
      args.add(LIMIT).add(limit[0]).add(limit[1]);
    }

    if (params != null && !params.isEmpty()) {
      args.add(PARAMS).add(params.size() << 1);
      params.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
    }

    if (dialect != null) {
      args.add(DIALECT).add(dialect);
    }
  }
