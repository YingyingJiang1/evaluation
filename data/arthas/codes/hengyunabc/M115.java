    public boolean verifyRemote(Socket socket)
    {
        InetAddress host1, host2;

        host1 = socket.getInetAddress();
        host2 = getRemoteAddress();

        return host1.equals(host2);
    }
