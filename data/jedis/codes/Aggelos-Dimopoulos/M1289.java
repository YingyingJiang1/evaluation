  @Override
  public String toString(Parenthesize mode) {
    String ret = super.toString(Parenthesize.NEVER);
    if (shouldParenthesize(mode)) {
      return "~(" + ret + ")";
    }
    return "~" + ret;
  }
