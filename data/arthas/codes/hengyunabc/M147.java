    final synchronized void _sendCommand(byte cmd) throws IOException
    {
            _output_.write(TelnetCommand.IAC);
            _output_.write(cmd);
            _output_.flush();
    }
