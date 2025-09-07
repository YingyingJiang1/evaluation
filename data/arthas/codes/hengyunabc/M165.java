    @Override
    public void disconnect() throws IOException
    {
        if (__input != null) {
            __input.close();
        }
        if (__output != null) {
            __output.close();
        }
        super.disconnect();
    }
