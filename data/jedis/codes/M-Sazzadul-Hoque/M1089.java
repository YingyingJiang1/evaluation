    public Connection getConnection() {
        Cluster cluster = provider.getCluster(); // Pass this by reference for thread safety

        DecorateSupplier<Connection> supplier = Decorators.ofSupplier(() -> this.handleGetConnection(cluster));

        supplier.withRetry(cluster.getRetry());
        supplier.withCircuitBreaker(cluster.getCircuitBreaker());
        supplier.withFallback(provider.getFallbackExceptionList(),
                e -> this.handleClusterFailover(cluster.getCircuitBreaker()));

        return supplier.decorate().get();
    }
