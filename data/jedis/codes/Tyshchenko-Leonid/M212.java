  @Override
  public int hashCode() {
    int result = Objects.hash(version);
    result = 31 * result + Arrays.hashCode(opargs);
    return result;
  }
