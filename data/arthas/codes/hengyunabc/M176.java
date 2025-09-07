    public void open(int port, InetAddress laddr) throws SocketException
    {
        _socket_ = _socketFactory_.createDatagramSocket(port, laddr);
        _socket_.setSoTimeout(_timeout_);
        _isOpen_ = true;
    }
