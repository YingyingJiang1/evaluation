  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder().append(getClass().getSimpleName())
        .append("{key=").append(getKey()).append(", labels=").append(labels);
    if (aggregators != null) {
      sb.append(", aggregators=").append(aggregators);
    }
    if (reducers != null && sources != null) {
      sb.append(", reducers").append(reducers).append(", sources").append(sources);
    }
    return sb.append(", elements=").append(getElements()).append('}').toString();
  }
