    public Observable<ResponseType> toObservable(Scheduler observeOn) {

        return Observable.defer(new Func0<Observable<ResponseType>>() {
            @Override
            public Observable<ResponseType> call() {
                final boolean isRequestCacheEnabled = getProperties().requestCacheEnabled().get();

                /* try from cache first */
                if (isRequestCacheEnabled) {
                    HystrixCachedObservable<ResponseType> fromCache = requestCache.get(getCacheKey());
                    if (fromCache != null) {
                        metrics.markResponseFromCache();
                        return fromCache.toObservable();
                    }
                }

                RequestCollapser<BatchReturnType, ResponseType, RequestArgumentType> requestCollapser = collapserFactory.getRequestCollapser(collapserInstanceWrapper);
                Observable<ResponseType> response = requestCollapser.submitRequest(getRequestArgument());
                metrics.markRequestBatched();
                if (isRequestCacheEnabled) {
                    /*
                     * A race can occur here with multiple threads queuing but only one will be cached.
                     * This means we can have some duplication of requests in a thread-race but we're okay
                     * with having some inefficiency in duplicate requests in the same batch
                     * and then subsequent requests will retrieve a previously cached Observable.
                     *
                     * If this is an issue we can make a lazy-future that gets set in the cache
                     * then only the winning 'put' will be invoked to actually call 'submitRequest'
                     */
                    HystrixCachedObservable<ResponseType> toCache = HystrixCachedObservable.from(response);
                    HystrixCachedObservable<ResponseType> fromCache = requestCache.putIfAbsent(getCacheKey(), toCache);
                    if (fromCache == null) {
                        return toCache.toObservable();
                    } else {
                        return fromCache.toObservable();
                    }
                }
                return response;
            }
        });
    }
