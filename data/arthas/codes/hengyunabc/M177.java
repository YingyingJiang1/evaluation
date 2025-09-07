    public void close()
    {
        if (_socket_ != null) {
            _socket_.close();
        }
        _socket_ = null;
        _isOpen_ = false;
    }
