    final synchronized void _requestDo(int option)
    throws IOException
    {
        if ((_doResponse[option] == 0 && _stateIsDo(option))
            || _requestedDo(option))
        {
            return ;
        }
        _setWantDo(option);
        ++_doResponse[option];
        _sendDo(option);
    }
