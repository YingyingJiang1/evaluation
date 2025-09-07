  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SetParams setParams = (SetParams) o;
    return Objects.equals(existance, setParams.existance) && super.equals((BaseSetExParams) o);
  }
