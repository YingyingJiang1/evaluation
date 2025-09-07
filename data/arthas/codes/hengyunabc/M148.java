    final synchronized void _processAYTResponse()
    {
        if (!aytFlag)
        {
            synchronized (aytMonitor)
            {
                aytFlag = true;
                aytMonitor.notifyAll();
            }
        }
    }
