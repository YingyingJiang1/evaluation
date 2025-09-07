  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Arrays.hashCode(this.member);
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.distance) ^ (Double.doubleToLongBits(this.distance) >>> 32));
    hash = 67 * hash + Objects.hashCode(this.coordinate);
    hash = 67 * hash + (int) (this.rawScore ^ (this.rawScore >>> 32));
    return hash;
  }
