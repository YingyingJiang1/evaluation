    public void connect(String hostname) throws SocketException, IOException
    {
        connect(hostname, _defaultPort_);
        _hostname_ = hostname;
    }
