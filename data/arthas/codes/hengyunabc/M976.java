    private void checkCallHandler() {
        if (handler != null && isComplete()) {
            handler.handle(this);
        }
    }
