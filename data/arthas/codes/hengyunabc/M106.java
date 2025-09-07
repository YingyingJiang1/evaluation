    public void connect(InetAddress host, int port,
                        InetAddress localAddr, int localPort)
    throws SocketException, IOException
    {
        _hostname_ = null;
        _socket_ = _socketFactory_.createSocket();
        if (receiveBufferSize != -1) {
            _socket_.setReceiveBufferSize(receiveBufferSize);
        }
        if (sendBufferSize != -1) {
            _socket_.setSendBufferSize(sendBufferSize);
        }
        _socket_.bind(new InetSocketAddress(localAddr, localPort));
        _socket_.connect(new InetSocketAddress(host, port), connectTimeout);
        _connectAction_();
    }
