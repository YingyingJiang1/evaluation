    @Override
    public void write(byte buffer[], int offset, int length) throws IOException
    {
        synchronized (__client)
        {
            while (length-- > 0) {
                write(buffer[offset++]);
            }
        }
    }
