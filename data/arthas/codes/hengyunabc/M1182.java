    public void handleIntr(Integer key) {
        if (interruptHandler == null || !interruptHandler.deliver(key)) {
            echo(key, '\n');
        }
    }
