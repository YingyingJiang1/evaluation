  private String getKeyTag(String key) {
    if (tagPattern != null) {
      Matcher m = tagPattern.matcher(key);
      if (m.find()) return m.group(1);
    }
    return key;
  }
