    @Override
    public void protocolReplyReceived(ProtocolCommandEvent event)
    {
        if (__directionMarker) {
            __writer.print("< ");
        }
        __writer.print(event.getMessage());
        __writer.flush();
    }
