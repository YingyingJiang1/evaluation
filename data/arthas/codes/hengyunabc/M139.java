    void _processCommand(int command)
    {
        if (debugoptions)
        {
            System.err.println("RECEIVED COMMAND: " + command);
        }

        if (__notifhand != null)
        {
            __notifhand.receivedNegotiation(
                TelnetNotificationHandler.RECEIVED_COMMAND, command);
        }
    }
