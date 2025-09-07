    public void setServerSocketFactory(ServerSocketFactory factory) {
        if (factory == null) {
            _serverSocketFactory_ = __DEFAULT_SERVER_SOCKET_FACTORY;
        } else {
            _serverSocketFactory_ = factory;
        }
    }
