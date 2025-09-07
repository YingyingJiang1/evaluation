  private String toStringCombinable(Parenthesize mode) {
    StringBuilder sb = new StringBuilder(formatField());
    if (values.length > 1 || mode == Parenthesize.ALWAYS) {
      sb.append('(');
    }
    StringJoiner sj = new StringJoiner(joinString);
    for (Value v : values) {
      sj.add(v.toString());
    }
    sb.append(sj.toString());
    if (values.length > 1 || mode == Parenthesize.ALWAYS) {
      sb.append(')');
    }
    return sb.toString();
  }
