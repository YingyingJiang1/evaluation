  public static void closeQuietly(AutoCloseable resource) {
    // It's same thing as Apache Commons - IOUtils.closeQuietly()
    if (resource != null) {
      try {
        resource.close();
      } catch (Exception e) {
        // ignored
      }
    }
  }
