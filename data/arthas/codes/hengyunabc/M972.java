    public void complete(T result) {
        checkComplete();
        this.result = result;
        succeeded = true;
        checkCallHandler();
    }
