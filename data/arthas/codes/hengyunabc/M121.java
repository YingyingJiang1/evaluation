    void _start()
    {
        if(__thread == null) {
            return;
        }

        int priority;
        __isClosed = false;
        // TODO remove this
        // Need to set a higher priority in case JVM does not use pre-emptive
        // threads.  This should prevent scheduler induced deadlock (rather than
        // deadlock caused by a bug in this code).
        priority = Thread.currentThread().getPriority() + 1;
        if (priority > Thread.MAX_PRIORITY) {
            priority = Thread.MAX_PRIORITY;
        }
        __thread.setPriority(priority);
        __thread.setDaemon(true);
        __thread.start();
        __threaded = true; // tell _processChar that we are running threaded
    }
