  protected HostAndPort getSocketHostAndPort() {
    HostAndPortMapper mapper = hostAndPortMapper;
    HostAndPort hap = this.hostAndPort;
    if (mapper != null) {
      HostAndPort mapped = mapper.getHostAndPort(hap);
      if (mapped != null) {
        return mapped;
      }
    }
    return hap;
  }
