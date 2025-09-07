    public void connect(InetAddress host) throws SocketException, IOException
    {
        _hostname_ = null;
        connect(host, _defaultPort_);
    }
