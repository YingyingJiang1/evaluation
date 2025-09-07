    private Connection handleGetConnection(Cluster cluster) {
        Connection connection = cluster.getConnection();
        connection.ping();
        return connection;
    }
