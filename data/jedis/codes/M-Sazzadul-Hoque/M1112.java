  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Path2)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return this.toString().equals(((Path2) obj).toString());
  }
