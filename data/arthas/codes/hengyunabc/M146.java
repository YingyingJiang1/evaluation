    final synchronized void _sendSubnegotiation(int subn[])
    throws IOException
    {
        if (debug)
        {
            System.err.println("SEND SUBNEGOTIATION: ");
            if (subn != null)
            {
                System.err.println(Arrays.toString(subn));
            }
        }
        if (subn != null)
        {
            _output_.write(_COMMAND_SB);
            // Note _output_ is buffered, so might as well simplify by writing single bytes
            for (int element : subn)
            {
                byte b = (byte) element;
                if (b == (byte) TelnetCommand.IAC) { // cast is necessary because IAC is outside the signed byte range
                    _output_.write(b); // double any IAC bytes
                }
                _output_.write(b);
            }
            _output_.write(_COMMAND_SE);

            /* Code Section added for sending the negotiation ASAP (start)*/
            _output_.flush();
            /* Code Section added for sending the negotiation ASAP (end)*/
        }
    }
