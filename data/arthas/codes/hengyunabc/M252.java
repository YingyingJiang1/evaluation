    @Override
    public AgentClusterInfo findAgent(String agentId) {

        ValueWrapper valueWrapper = cache.get(agentId);
        if (valueWrapper == null) {
            return null;
        }

        AgentClusterInfo info = (AgentClusterInfo) valueWrapper.get();
        return info;
    }
