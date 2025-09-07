  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    final CacheKey other = (CacheKey) obj;
    return Objects.equals(this.command, other.command);
  }
