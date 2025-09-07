  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    appendFrom(sb, inclusiveMin);
    sb.append(' ');
    appendTo(sb, inclusiveMax);
    sb.append(']');
    return sb.toString();
  }
