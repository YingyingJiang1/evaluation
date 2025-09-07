            @Override
            public void handle(Future<T> ar) {
                if (ar.succeeded()) {
                    complete(ar.result());
                } else {
                    fail(ar.cause());
                }
            }
