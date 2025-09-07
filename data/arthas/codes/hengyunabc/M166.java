    public boolean getLocalOptionState(int option)
    {
        /* BUG (option active when not already acknowledged) (start)*/
        return (_stateIsWill(option) && _requestedWill(option));
        /* BUG (option active when not already acknowledged) (end)*/
    }
