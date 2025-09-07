  private String formatField() {
    if (field == null || field.isEmpty()) {
      return "";
    }
    return '@' + field + ':';
  }
