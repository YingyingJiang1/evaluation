  public TagField separator(char separator) {
    if (separator < 128) {
      this.separator = new byte[]{(byte) separator};
    } else {
      this.separator = SafeEncoder.encode(String.valueOf(separator));
    }
    return this;
  }
