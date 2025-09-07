  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModuleLoadExParams that = (ModuleLoadExParams) o;
    return Objects.equals(configs, that.configs) && Objects.equals(args, that.args);
  }
