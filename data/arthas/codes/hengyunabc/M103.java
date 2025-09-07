    protected void _connectAction_() throws IOException
    {
        _socket_.setSoTimeout(_timeout_);
        _input_ = _socket_.getInputStream();
        _output_ = _socket_.getOutputStream();
    }
