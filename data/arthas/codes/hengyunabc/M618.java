    @Option(shortName = "b", longName = "include-blocking-thread", flag = true)
    @Description("Find the thread who is holding a lock that blocks the most number of threads.")
    public void setFindMostBlockingThread(boolean findMostBlockingThread) {
        this.findMostBlockingThread = findMostBlockingThread;
    }
