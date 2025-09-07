  @Override
  public String toString() {
    return new StringBuilder().append(getClass().getSimpleName())
        .append("{key=").append(getKey())
        .append(", labels=").append(labels)
        .append(", element=").append(getElement())
        .append('}').toString();
  }
