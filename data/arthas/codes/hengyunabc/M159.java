    final boolean _sendAYT(long timeout)
    throws IOException, IllegalArgumentException, InterruptedException
    {
        boolean retValue = false;
        synchronized (aytMonitor)
        {
            synchronized (this)
            {
                aytFlag = false;
                _output_.write(_COMMAND_AYT);
                _output_.flush();
            }
            aytMonitor.wait(timeout);
            if (!aytFlag)
            {
                retValue = false;
                aytFlag = true;
            }
            else
            {
                retValue = true;
            }
        }

        return (retValue);
    }
