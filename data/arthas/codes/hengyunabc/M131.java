    @Override
    public void write(int ch) throws IOException
    {

        synchronized (__client)
        {
            ch &= 0xff;

            if (__client._requestedWont(TelnetOption.BINARY)) // i.e. ASCII
            {
                if (__lastWasCR)
                {
                    if (__convertCRtoCRLF)
                    {
                        __client._sendByte('\n');
                        if (ch == '\n') // i.e. was CRLF anyway
                        {
                            __lastWasCR = false;
                            return ;
                        }
                    } // __convertCRtoCRLF
                    else if (ch != '\n')
                     {
                        __client._sendByte('\0'); // RFC854 requires CR NUL for bare CR
                    }
                }

                switch (ch)
                {
                case '\r':
                    __client._sendByte('\r');
                    __lastWasCR = true;
                    break;
                case '\n':
                    if (!__lastWasCR) { // convert LF to CRLF
                        __client._sendByte('\r');
                    }
                    __client._sendByte(ch);
                    __lastWasCR = false;
                    break;
                case TelnetCommand.IAC:
                    __client._sendByte(TelnetCommand.IAC);
                    __client._sendByte(TelnetCommand.IAC);
                    __lastWasCR = false;
                    break;
                default:
                    __client._sendByte(ch);
                    __lastWasCR = false;
                    break;
                }
            } // end ASCII
            else if (ch == TelnetCommand.IAC)
            {
                __client._sendByte(ch);
                __client._sendByte(TelnetCommand.IAC);
            } else {
                __client._sendByte(ch);
            }
        }
    }
