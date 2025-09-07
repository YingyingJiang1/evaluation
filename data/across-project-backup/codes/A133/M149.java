    @Override
    protected void _connectAction_() throws IOException
    {
        /* (start). BUGFIX: clean the option info for each connection*/
        for (int ii = 0; ii < TelnetOption.MAX_OPTION_VALUE + 1; ii++)
        {
            _doResponse[ii] = 0;
            _willResponse[ii] = 0;
            _options[ii] = 0;
            if (optionHandlers[ii] != null)
            {
                optionHandlers[ii].setDo(false);
                optionHandlers[ii].setWill(false);
            }
        }
        /* (end). BUGFIX: clean the option info for each connection*/

        super._connectAction_();
        _input_ = new BufferedInputStream(_input_);
        _output_ = new BufferedOutputStream(_output_);

        /* open TelnetOptionHandler functionality (start)*/
        for (int ii = 0; ii < TelnetOption.MAX_OPTION_VALUE + 1; ii++)
        {
            if (optionHandlers[ii] != null)
            {
                if (optionHandlers[ii].getInitLocal())
                {
                    _requestWill(optionHandlers[ii].getOptionCode());
                }

                if (optionHandlers[ii].getInitRemote())
                {
                    _requestDo(optionHandlers[ii].getOptionCode());
                }
            }
        }
        /* open TelnetOptionHandler functionality (end)*/
    }
