    void deleteOptionHandler(int optcode)
    throws InvalidTelnetOptionException, IOException
    {
        if (TelnetOption.isValidOption(optcode))
        {
            if (optionHandlers[optcode] == null)
            {
                throw (new InvalidTelnetOptionException(
                    "Unregistered option", optcode));
            }
            else
            {
                TelnetOptionHandler opthand = optionHandlers[optcode];
                optionHandlers[optcode] = null;

                if (opthand.getWill())
                {
                    _requestWont(optcode);
                }

                if (opthand.getDo())
                {
                    _requestDont(optcode);
                }
            }
        }
        else
        {
            throw (new InvalidTelnetOptionException(
                "Invalid Option Code", optcode));
        }
    }
