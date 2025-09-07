    final synchronized void _sendWill(int option)
    throws IOException
    {
        if (debug || debugoptions)
        {
            System.err.println("WILL: " + TelnetOption.getOption(option));
        }
        _output_.write(_COMMAND_WILL);
        _output_.write(option);

        /* Code Section added for sending the negotiation ASAP (start)*/
        _output_.flush();
        /* Code Section added for sending the negotiation ASAP (end)*/
    }
