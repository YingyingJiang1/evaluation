  private String toStringDefault(Parenthesize mode) {
    boolean useParen = mode == Parenthesize.ALWAYS;
    if (!useParen) {
      useParen = mode != Parenthesize.NEVER && values.length > 1;
    }
    StringBuilder sb = new StringBuilder();
    if (useParen) {
      sb.append('(');
    }
    StringJoiner sj = new StringJoiner(joinString);
    for (Value v : values) {
      sj.add(formatField() + v.toString());
    }
    sb.append(sj.toString());
    if (useParen) {
      sb.append(')');
    }
    return sb.toString();
  }
