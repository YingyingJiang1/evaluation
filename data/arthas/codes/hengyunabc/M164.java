    @Override
    protected void _connectAction_() throws IOException
    {
        super._connectAction_();
        TelnetInputStream tmp = new TelnetInputStream(_input_, this, readerThread);
        if(readerThread)
        {
            tmp._start();
        }
        // __input CANNOT refer to the TelnetInputStream.  We run into
        // blocking problems when some classes use TelnetInputStream, so
        // we wrap it with a BufferedInputStream which we know is safe.
        // This blocking behavior requires further investigation, but right
        // now it looks like classes like InputStreamReader are not implemented
        // in a safe manner.
        __input = new BufferedInputStream(tmp);
        __output = new TelnetOutputStream(this);
    }
