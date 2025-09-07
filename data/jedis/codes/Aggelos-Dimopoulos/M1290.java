  @Override
  public String toString(Parenthesize parenMode) {
    StringBuilder sb = new StringBuilder();
    StringJoiner sj = new StringJoiner(getJoinString());
    if (shouldParenthesize(parenMode)) {
      sb.append('(');
    }
    for (Node n : children) {
      sj.add(n.toString(parenMode));
    }
    sb.append(sj.toString());
    if (shouldParenthesize(parenMode)) {
      sb.append(')');
    }
    return sb.toString();
  }
