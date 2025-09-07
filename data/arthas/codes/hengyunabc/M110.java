    public void disconnect() throws IOException
    {
        closeQuietly(_socket_);
        closeQuietly(_input_);
        closeQuietly(_output_);
        _socket_ = null;
        _hostname_ = null;
        _input_ = null;
        _output_ = null;
    }
