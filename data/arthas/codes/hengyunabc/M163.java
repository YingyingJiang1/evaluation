    void _spyWrite(int ch)
    {
        if (!(_stateIsDo(TelnetOption.ECHO)
            && _requestedDo(TelnetOption.ECHO)))
        {
            OutputStream spy = spyStream;
            if (spy != null)
            {
                try
                {
                    spy.write(ch);
                    spy.flush();
                }
                catch (IOException e)
                {
                    spyStream = null;
                }
            }
        }
    }
