    public boolean getRemoteOptionState(int option)
    {
        /* BUG (option active when not already acknowledged) (start)*/
        return (_stateIsDo(option) && _requestedDo(option));
        /* BUG (option active when not already acknowledged) (end)*/
    }
