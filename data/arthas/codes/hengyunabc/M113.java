    public boolean isConnected()
    {
        if (_socket_ == null) {
            return false;
        }

        return _socket_.isConnected();
    }
