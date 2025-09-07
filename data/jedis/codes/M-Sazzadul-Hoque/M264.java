  @Override
  public void close() {
    if (dataSource != null) {
      Pool<Jedis> pool = this.dataSource;
      this.dataSource = null;
      if (isBroken()) {
        pool.returnBrokenResource(this);
      } else {
        pool.returnResource(this);
      }
    } else {
      connection.close();
    }
  }
