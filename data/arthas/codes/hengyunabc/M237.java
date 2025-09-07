    public void addProxyRequestPromise(String requestId, Promise<SimpleHttpResponse> promise) {
        this.proxyRequestPromiseMap.put(requestId, promise);
        // 把过期的proxy 请求删掉
        workerGroup.schedule(new Runnable() {

            @Override
            public void run() {
                removeProxyRequestPromise(requestId);
            }

        }, 60, TimeUnit.SECONDS);
    }
