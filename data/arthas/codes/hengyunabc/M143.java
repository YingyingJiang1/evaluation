    void _processWont(int option) throws IOException
    {
        if (debugoptions)
        {
            System.err.println("RECEIVED WONT: "
                + TelnetOption.getOption(option));
        }

        if (__notifhand != null)
        {
            __notifhand.receivedNegotiation(
                TelnetNotificationHandler.RECEIVED_WONT,
                option);
        }

        if (_doResponse[option] > 0)
        {
            --_doResponse[option];
            if (_doResponse[option] > 0 && _stateIsDont(option))
            {
                --_doResponse[option];
            }
        }

        if (_doResponse[option] == 0 && _requestedDo(option))
        {

            switch (option)
            {

            default:
                break;

            }

            /* FIX for a BUG in the negotiation (start)*/
            if ((_stateIsDo(option)) || (_requestedDo(option)))
            {
                _sendDont(option);
            }

            _setWantDont(option);
            /* FIX for a BUG in the negotiation (end)*/
        }

        _setDont(option);
    }
