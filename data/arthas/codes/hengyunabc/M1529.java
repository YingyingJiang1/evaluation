    private void filterThread(Thread thread, boolean enable) {
        if (thread == null || thread == Thread.currentThread()) {
            filterThread0(null, enable);
        } else {
            // Need to take lock to avoid race condition with a thread state change
            synchronized (thread) {
                Thread.State state = thread.getState();
                if (state != Thread.State.NEW && state != Thread.State.TERMINATED) {
                    filterThread0(thread, enable);
                }
            }
        }
    }
