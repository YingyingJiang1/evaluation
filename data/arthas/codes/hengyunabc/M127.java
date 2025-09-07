    @Override
    public int available() throws IOException
    {
        // Critical section because run() may change __bytesAvailable
        synchronized (__queue)
        {
            if (__threaded) { // Must not call super.available when running threaded: NET-466
                return __bytesAvailable;
            } else {
                return __bytesAvailable + super.available();
            }
        }
    }
