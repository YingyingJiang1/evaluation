    @Override
    public void handle(String event) {
        if ("q".equalsIgnoreCase(event)) {
            process.end();
        }
    }
