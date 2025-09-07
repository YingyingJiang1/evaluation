  @Override
  public int hashCode() {
    int hashCode = 1;
    for (Rawable e : arguments) {
      hashCode = 31 * hashCode + e.hashCode();
    }
    hashCode = 31 * hashCode + builder.hashCode();
    return hashCode;
  }
