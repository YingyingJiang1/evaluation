    final synchronized void _sendWont(int option)
    throws IOException
    {
        if (debug || debugoptions)
        {
            System.err.println("WONT: " + TelnetOption.getOption(option));
        }
        _output_.write(_COMMAND_WONT);
        _output_.write(option);

        /* Code Section added for sending the negotiation ASAP (start)*/
        _output_.flush();
        /* Code Section added for sending the negotiation ASAP (end)*/
    }
