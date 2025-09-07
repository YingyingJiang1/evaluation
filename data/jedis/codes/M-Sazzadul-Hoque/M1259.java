  public Query highlightFields(HighlightTags tags, String... fields) {
    if (fields == null || fields.length > 0) {
      highlightFields = fields;
    }
    if (tags != null) {
      highlightTags = new String[]{tags.open, tags.close};
    } else {
      highlightTags = null;
    }
    wantsHighlight = true;
    return this;
  }
