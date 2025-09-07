    public void fail(Throwable throwable) {
        checkComplete();
        this.throwable = throwable;
        failed = true;
        checkCallHandler();
    }
