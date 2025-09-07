  @Override
  public int available() throws IOException {
    int availableInBuf = limit - count;
    int availableInSocket = this.in.available();
    return (availableInBuf > availableInSocket) ? availableInBuf : availableInSocket;
  }
