    @ReadOperation
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap<>(4);

        result.put("version", this.getClass().getPackage().getImplementationVersion());
        result.put("properties", arthasProperties);

        result.put("agents", tunnelServer.getAgentInfoMap());
        result.put("clientConnections", tunnelServer.getClientConnectionInfoMap());

        return result;
    }
