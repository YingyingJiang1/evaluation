    public void setOnCancelHandler() {
        ServerCallStreamObserver<T> observer = (ServerCallStreamObserver<T>) this.streamObserver;
        observer.setOnCancelHandler(() -> {
            this.end();
        });
    }
