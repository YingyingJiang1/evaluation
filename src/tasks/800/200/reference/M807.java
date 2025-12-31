    @Override
    public void subscribe(Subscriber<? super R>[] subscribers) {
        subscribers = RxJavaPlugins.onSubscribe(this, subscribers);

        if (!validate(subscribers)) {
            return;
        }

        int n = subscribers.length;
        @SuppressWarnings("unchecked")
        Subscriber<T>[] parents = new Subscriber[n];

        for (int i = 0; i < n; i++) {

            R initialValue;

            try {
                initialValue = Objects.requireNonNull(initialSupplier.get(), "The initialSupplier returned a null value");
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                reportError(subscribers, ex);
                return;
            }

            parents[i] = new ParallelReduceSubscriber<>(subscribers[i], initialValue, reducer);
        }

        source.subscribe(parents);
    }
