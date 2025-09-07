    public void fireReplyReceived(int replyCode, String message)
    {
        ProtocolCommandEvent event;
        event = new ProtocolCommandEvent(__source, replyCode, message);

        for (EventListener listener : __listeners)
        {
            ((ProtocolCommandListener)listener).protocolReplyReceived(event);
        }
    }
