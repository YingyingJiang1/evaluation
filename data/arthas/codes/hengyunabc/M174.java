    public void open() throws SocketException
    {
        _socket_ = _socketFactory_.createDatagramSocket();
        _socket_.setSoTimeout(_timeout_);
        _isOpen_ = true;
    }
