    public void fireCommandSent(String command, String message)
    {
        ProtocolCommandEvent event;

        event = new ProtocolCommandEvent(__source, command, message);

        for (EventListener listener : __listeners)
        {
           ((ProtocolCommandListener)listener).protocolCommandSent(event);
        }
    }
