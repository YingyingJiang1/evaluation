    @Override
    public Socket createSocket(InetAddress address, int port,
                               InetAddress localAddr, int localPort)
    throws IOException
    {
        if (connProxy != null)
        {
            Socket s = new Socket(connProxy);
            s.bind(new InetSocketAddress(localAddr, localPort));
            s.connect(new InetSocketAddress(address, port));
            return s;
        }
        return new Socket(address, port, localAddr, localPort);
    }
