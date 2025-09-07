    public void open(int port) throws SocketException
    {
        _socket_ = _socketFactory_.createDatagramSocket(port);
        _socket_.setSoTimeout(_timeout_);
        _isOpen_ = true;
    }
