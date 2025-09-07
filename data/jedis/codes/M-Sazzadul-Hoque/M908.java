  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if (!(obj instanceof TSElement)) return false;

    TSElement other = (TSElement) obj;
    return this.timestamp == other.timestamp
        && this.value == other.value;
  }
