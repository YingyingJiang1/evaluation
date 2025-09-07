    public void handleEof(Integer key) {
        // Pseudo signal
        if (stdinHandler != null) {
            stdinHandler.handle(Helper.fromCodePoints(new int[]{key}));
        } else {
            echo(key);
            readline.queueEvent(new int[]{key});
        }
    }
