    @Override
    public Socket createSocket() throws IOException
    {
        if (connProxy != null)
        {
            return new Socket(connProxy);
        }
        return new Socket();
    }
