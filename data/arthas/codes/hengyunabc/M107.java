    public void connect(String hostname, int port,
                        InetAddress localAddr, int localPort)
    throws SocketException, IOException
    {
       connect(InetAddress.getByName(hostname), port, localAddr, localPort);
       _hostname_ = hostname;
    }
