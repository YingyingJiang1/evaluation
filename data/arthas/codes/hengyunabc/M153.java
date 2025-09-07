    final synchronized void _requestDont(int option)
    throws IOException
    {
        if ((_doResponse[option] == 0 && _stateIsDont(option))
            || _requestedDont(option))
        {
            return ;
        }
        _setWantDont(option);
        ++_doResponse[option];
        _sendDont(option);
    }
