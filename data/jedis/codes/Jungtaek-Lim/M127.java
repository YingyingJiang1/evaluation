  private void build() {
    // check build state to prevent recursion
    if (building) {
      return;
    }

    building = true;
    try {
      if (data != null) {
        if (data instanceof JedisDataException) {
          exception = (JedisDataException) data;
        } else {
          response = builder.build(data);
        }
      }

      data = null;
    } finally {
      building = false;
      built = true;
    }
  }
