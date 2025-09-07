    @Override
    public Socket createSocket(String host, int port,
                               InetAddress localAddr, int localPort)
    throws UnknownHostException, IOException
    {
        if (connProxy != null)
        {
            Socket s = new Socket(connProxy);
            s.bind(new InetSocketAddress(localAddr, localPort));
            s.connect(new InetSocketAddress(host, port));
            return s;
        }
        return new Socket(host, port, localAddr, localPort);
    }
