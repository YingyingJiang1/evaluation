            @Override
            public Observable<ResponseType> call() {
                final boolean isRequestCacheEnabled = getProperties().requestCacheEnabled().get();
                final String cacheKey = getCacheKey();

                /* try from cache first */
                if (isRequestCacheEnabled) {
                    HystrixCachedObservable<ResponseType> fromCache = requestCache.get(cacheKey);
                    if (fromCache != null) {
                        metrics.markResponseFromCache();
                        return fromCache.toObservable();
                    }
                }

                RequestCollapser<BatchReturnType, ResponseType, RequestArgumentType> requestCollapser = collapserFactory.getRequestCollapser(collapserInstanceWrapper);
                Observable<ResponseType> response = requestCollapser.submitRequest(getRequestArgument());

                if (isRequestCacheEnabled && cacheKey != null) {
                    HystrixCachedObservable<ResponseType> toCache = HystrixCachedObservable.from(response);
                    HystrixCachedObservable<ResponseType> fromCache = requestCache.putIfAbsent(cacheKey, toCache);
                    if (fromCache == null) {
                        return toCache.toObservable();
                    } else {
                        toCache.unsubscribe();
                        return fromCache.toObservable();
                    }
                }
                return response;
            }
