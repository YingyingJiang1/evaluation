  public static HostAndPort from(String string) {
    int lastColon = string.lastIndexOf(":");
    String host = string.substring(0, lastColon);
    int port = Integer.parseInt(string.substring(lastColon + 1));
    return new HostAndPort(host, port);
  }
