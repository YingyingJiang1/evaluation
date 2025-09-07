  public Query summarizeFields(int contextLen, int fragmentCount, String separator, String... fields) {
    if (fields == null || fields.length > 0) {
      summarizeFields = fields;
    }
    summarizeFragmentLen = contextLen;
    summarizeNumFragments = fragmentCount;
    summarizeSeparator = separator;
    wantsSummarize = true;
    return this;
  }
