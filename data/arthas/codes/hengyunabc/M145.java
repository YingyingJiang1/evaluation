    final synchronized void _sendTerminalType()
    throws IOException
    {
        if (debug)
        {
            System.err.println("SEND TERMINAL-TYPE: " + terminalType);
        }
        if (terminalType != null)
        {
            _output_.write(_COMMAND_SB);
            _output_.write(_COMMAND_IS);
            _output_.write(terminalType.getBytes(getCharset()));
            _output_.write(_COMMAND_SE);
            _output_.flush();
        }
    }
