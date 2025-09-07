        @Override
        public Subscriber<? super R> call(final Subscriber<? super R> subscriber) {
            return new Subscriber<R>(subscriber) {
                @Override
                public void onCompleted() {
                    subscriber.onCompleted();
                }

                @Override
                public void onError(Throwable t) {
                    //no need to call a hook here.  FallbackHookApplication is already calling the proper and non-deprecated hook
                    subscriber.onError(t);
                }

                @Override
                public void onNext(R r) {
                    try {
                        R wrappedValue = executionHook.onFallbackSuccess(cmd, r);
                        subscriber.onNext(wrappedValue);
                    } catch (Throwable hookEx) {
                        logger.warn("Error calling HystrixCommandExecutionHook.onFallbackSuccess", hookEx);
                        subscriber.onNext(r);
                    }
                }
            };
        }
