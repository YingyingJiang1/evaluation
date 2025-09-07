  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Path)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return this.toString().equals(((Path) obj).toString());
  }
