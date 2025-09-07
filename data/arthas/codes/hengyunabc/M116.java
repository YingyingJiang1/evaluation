    public void setSocketFactory(SocketFactory factory)
    {
        if (factory == null) {
            _socketFactory_ = __DEFAULT_SOCKET_FACTORY;
        } else {
            _socketFactory_ = factory;
        }
        // re-setting the socket factory makes the proxy setting useless,
        // so set the field to null so that getProxy() doesn't return a
        // Proxy that we're actually not using.
        connProxy = null;
    }
