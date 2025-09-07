  @Override
  public String toString(Parenthesize mode) {
    if (values[0].isCombinable()) {
      return toStringCombinable(mode);
    }
    return toStringDefault(mode);
  }
