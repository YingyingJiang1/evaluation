    public void handleSusp(Integer key) {
        if (suspendHandler == null || !suspendHandler.deliver(key)) {
            echo(key, 'Z' - 64);
        }
    }
