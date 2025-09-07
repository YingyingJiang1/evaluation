  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LolwutParams that = (LolwutParams) o;
    return Objects.equals(version, that.version) && Arrays.equals(opargs, that.opargs);
  }
