        @Override
        public Subscriber<? super R> call(final Subscriber<? super R> subscriber) {
            return new Subscriber<R>(subscriber) {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable t) {
                    Exception e = getExceptionFromThrowable(t);
                    try {
                        Exception wrappedEx = executionHook.onRunError(cmd, e);
                        subscriber.onError(wrappedEx);
                    } catch (Throwable hookEx) {
                        logger.warn("Error calling HystrixCommandExecutionHook.onRunError", hookEx);
                        subscriber.onError(e);
                    }
                }

                @Override
                public void onNext(R r) {
                    try {
                        R wrappedValue = executionHook.onRunSuccess(cmd, r);
                        subscriber.onNext(wrappedValue);
                    } catch (Throwable hookEx) {
                        logger.warn("Error calling HystrixCommandExecutionHook.onRunSuccess", hookEx);
                        subscriber.onNext(r);
                    }
                }
            };
        }
