    void _setWont(int option)
    {
        _options[option] &= ~_WILL_MASK;

        /* open TelnetOptionHandler functionality (start)*/
        if (optionHandlers[option] != null)
        {
            optionHandlers[option].setWill(false);
        }
        /* open TelnetOptionHandler functionality (end)*/
    }
