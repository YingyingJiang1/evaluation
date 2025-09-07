    final synchronized void _sendByte(int b)
    throws IOException
    {
        _output_.write(b);

        /* Code Section added for supporting spystreams (start)*/
        _spyWrite(b);
        /* Code Section added for supporting spystreams (end)*/

    }
