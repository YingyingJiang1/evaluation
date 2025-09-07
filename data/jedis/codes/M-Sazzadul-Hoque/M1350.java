  @Override
  public String toString() {
    return getClass().getSimpleName() + "{Total results:" + totalResults
        + ", Documents:" + documents
        + (warnings != null ? ", Warnings:" + warnings : "")
        + "}";
  }
