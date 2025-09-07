    public void setDatagramSocketFactory(DatagramSocketFactory factory)
    {
        if (factory == null) {
            _socketFactory_ = __DEFAULT_SOCKET_FACTORY;
        } else {
            _socketFactory_ = factory;
        }
    }
