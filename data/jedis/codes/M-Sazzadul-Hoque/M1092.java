    private <T> T handleExecuteCommand(CommandObject<T> commandObject, Cluster cluster) {
        try (Connection connection = cluster.getConnection()) {
            return connection.executeCommand(commandObject);
        }
    }
