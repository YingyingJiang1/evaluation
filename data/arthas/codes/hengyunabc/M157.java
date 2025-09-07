    final synchronized void _requestWont(int option)
    throws IOException
    {
        if ((_willResponse[option] == 0 && _stateIsWont(option))
            || _requestedWont(option))
        {
            return ;
        }
        _setWantWont(option);
        ++_doResponse[option];
        _sendWont(option);
    }
