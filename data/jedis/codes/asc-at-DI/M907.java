  @Override
  public int hashCode() {
    int result = Boolean.hashCode(latest);
    result = 31 * result + Boolean.hashCode(withLabels);
    result = 31 * result + Arrays.hashCode(selectedLabels);
    return result;
  }
