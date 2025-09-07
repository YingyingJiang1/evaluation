  public static String escape(String text, boolean querying) {
    char[] chars = text.toCharArray();

    StringBuilder sb = new StringBuilder();
    for (char ch : chars) {
      if (ESCAPE_CHARS.contains(ch)
          || (querying && ch == ' ')) {
        sb.append("\\");
      }
      sb.append(ch);
    }
    return sb.toString();
  }
