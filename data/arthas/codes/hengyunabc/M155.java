    final synchronized void _requestWill(int option)
    throws IOException
    {
        if ((_willResponse[option] == 0 && _stateIsWill(option))
            || _requestedWill(option))
        {
            return ;
        }
        _setWantWill(option);
        ++_doResponse[option];
        _sendWill(option);
    }
