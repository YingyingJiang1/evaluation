    void addOptionHandler(TelnetOptionHandler opthand)
    throws InvalidTelnetOptionException, IOException
    {
        int optcode = opthand.getOptionCode();
        if (TelnetOption.isValidOption(optcode))
        {
            if (optionHandlers[optcode] == null)
            {
                optionHandlers[optcode] = opthand;
                if (isConnected())
                {
                    if (opthand.getInitLocal())
                    {
                        _requestWill(optcode);
                    }

                    if (opthand.getInitRemote())
                    {
                        _requestDo(optcode);
                    }
                }
            }
            else
            {
                throw (new InvalidTelnetOptionException(
                    "Already registered option", optcode));
            }
        }
        else
        {
            throw (new InvalidTelnetOptionException(
                "Invalid Option Code", optcode));
        }
    }
