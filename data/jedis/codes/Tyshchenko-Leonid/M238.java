  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MigrateParams that = (MigrateParams) o;
    return copy == that.copy && replace == that.replace && Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }
