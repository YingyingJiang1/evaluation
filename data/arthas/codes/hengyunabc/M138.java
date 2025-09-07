    void _setDont(int option)
    {
        _options[option] &= ~_DO_MASK;

        /* open TelnetOptionHandler functionality (start)*/
        if (optionHandlers[option] != null)
        {
            optionHandlers[option].setDo(false);
        }
        /* open TelnetOptionHandler functionality (end)*/
    }
