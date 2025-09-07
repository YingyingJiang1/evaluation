    public void connect(String hostname, int port)
    throws SocketException, IOException
    {
        connect(InetAddress.getByName(hostname), port);
        _hostname_ = hostname;
    }
