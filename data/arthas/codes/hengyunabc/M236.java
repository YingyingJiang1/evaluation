    public AgentInfo removeAgent(String id) {
        AgentInfo agentInfo = agentInfoMap.remove(id);
        if (this.tunnelClusterStore != null) {
            this.tunnelClusterStore.removeAgent(id);
        }
        return agentInfo;
    }
