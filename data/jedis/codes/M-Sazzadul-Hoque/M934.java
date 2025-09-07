  private Socket createSslSocket(HostAndPort _hostAndPort, Socket socket) throws IOException, GeneralSecurityException {

    Socket plainSocket = socket;

    SSLSocketFactory _sslSocketFactory;
    SSLParameters _sslParameters;

    if (sslOptions != null) {

      SSLContext _sslContext = sslOptions.createSslContext();
      _sslSocketFactory = _sslContext.getSocketFactory();

      _sslParameters = sslOptions.getSslParameters();

    } else {

      _sslSocketFactory = this.sslSocketFactory;
      _sslParameters = this.sslParameters;
    }

    if (_sslSocketFactory == null) {
      _sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    SSLSocket sslSocket = (SSLSocket) _sslSocketFactory.createSocket(socket,
        _hostAndPort.getHost(), _hostAndPort.getPort(), true);

    if (_sslParameters != null) {
      sslSocket.setSSLParameters(_sslParameters);
    }

    // allowing HostnameVerifier for both SslOptions and legacy ssl config
    if (hostnameVerifier != null && !hostnameVerifier.verify(_hostAndPort.getHost(), sslSocket.getSession())) {
      String message = String.format("The connection to '%s' failed ssl/tls hostname verification.",
          _hostAndPort.getHost());
      throw new JedisConnectionException(message);
    }

    return new SSLSocketWrapper(sslSocket, plainSocket);
  }
