    @Override
    public <T> T executeCommand(CommandObject<T> commandObject) {
        Cluster cluster = provider.getCluster(); // Pass this by reference for thread safety

        DecorateSupplier<T> supplier = Decorators.ofSupplier(() -> this.handleExecuteCommand(commandObject, cluster));

        supplier.withRetry(cluster.getRetry());
        supplier.withCircuitBreaker(cluster.getCircuitBreaker());
        supplier.withFallback(provider.getFallbackExceptionList(),
                e -> this.handleClusterFailover(commandObject, cluster.getCircuitBreaker()));

        return supplier.decorate().get();
    }
