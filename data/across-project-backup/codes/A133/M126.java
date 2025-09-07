    @Override
    public int read(byte buffer[], int offset, int length) throws IOException
    {
        int ch, off;

        if (length < 1) {
            return 0;
        }

        // Critical section because run() may change __bytesAvailable
        synchronized (__queue)
        {
            if (length > __bytesAvailable) {
                length = __bytesAvailable;
            }
        }

        if ((ch = read()) == EOF) {
            return EOF;
        }

        off = offset;

        do
        {
            buffer[offset++] = (byte)ch;
        }
        while (--length > 0 && (ch = read()) != EOF);

        //__client._spyRead(buffer, off, offset - off);
        return (offset - off);
    }
