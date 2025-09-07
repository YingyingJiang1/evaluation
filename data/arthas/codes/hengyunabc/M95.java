    @Override
    public Socket createSocket(String host, int port)
    throws UnknownHostException, IOException
    {
        if (connProxy != null)
        {
            Socket s = new Socket(connProxy);
            s.connect(new InetSocketAddress(host, port));
            return s;
        }
        return new Socket(host, port);
    }
