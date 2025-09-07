    public Handler<Future<T>> completer() {
        return new Handler<Future<T>>() {
            @Override
            public void handle(Future<T> ar) {
                if (ar.succeeded()) {
                    complete(ar.result());
                } else {
                    fail(ar.cause());
                }
            }
        };
    }
