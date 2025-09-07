  public TSCreateParams ignore(long maxTimediff, double maxValDiff) {
    this.ignore = true;
    this.ignoreMaxTimediff = maxTimediff;
    this.ignoreMaxValDiff = maxValDiff;
    return this;
  }
