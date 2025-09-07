        @Override
        public Subscriber<? super R> call(final Subscriber<? super R> subscriber) {
            return new Subscriber<R>(subscriber) {
                @Override
                public void onCompleted() {
                    try {
                        executionHook.onFallbackSuccess(cmd);
                    } catch (Throwable hookEx) {
                        logger.warn("Error calling HystrixCommandExecutionHook.onFallbackSuccess", hookEx);
                    }
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    Exception wrappedEx = wrapWithOnFallbackErrorHook(e);
                    subscriber.onError(wrappedEx);
                }

                @Override
                public void onNext(R r) {
                    R wrappedValue = wrapWithOnFallbackEmitHook(r);
                    subscriber.onNext(wrappedValue);
                }
            };
        }
