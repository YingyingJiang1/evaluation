    public void sendSubnegotiation(int[] message)
    throws IOException, IllegalArgumentException
    {
        if (message.length < 1) {
            throw new IllegalArgumentException("zero length message");
        }
        _sendSubnegotiation(message);
    }
