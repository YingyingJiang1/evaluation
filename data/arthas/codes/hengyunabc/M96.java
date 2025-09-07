    @Override
    public Socket createSocket(InetAddress address, int port)
    throws IOException
    {
        if (connProxy != null)
        {
            Socket s = new Socket(connProxy);
            s.connect(new InetSocketAddress(address, port));
            return s;
        }
        return new Socket(address, port);
    }
