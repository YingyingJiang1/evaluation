    private <T> T handleClusterFailover(CommandObject<T> commandObject, CircuitBreaker circuitBreaker) {

        clusterFailover(circuitBreaker);

        // Recursive call to the initiating method so the operation can be retried on the next cluster connection
        return executeCommand(commandObject);
    }
