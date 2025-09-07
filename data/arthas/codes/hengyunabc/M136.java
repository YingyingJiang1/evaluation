    void _setDo(int option) throws IOException
    {
        _options[option] |= _DO_MASK;

        /* open TelnetOptionHandler functionality (start)*/
        if (_requestedDo(option))
        {
            if (optionHandlers[option] != null)
            {
                optionHandlers[option].setDo(true);

                int subneg[] =
                    optionHandlers[option].startSubnegotiationRemote();

                if (subneg != null)
                {
                    _sendSubnegotiation(subneg);
                }
            }
        }
        /* open TelnetOptionHandler functionality (end)*/
    }
