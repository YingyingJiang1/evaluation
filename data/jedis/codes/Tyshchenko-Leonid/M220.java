  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommandListFilterByParams that = (CommandListFilterByParams) o;
    return Objects.equals(moduleName, that.moduleName) && Objects.equals(category, that.category) && Objects.equals(pattern, that.pattern);
  }
