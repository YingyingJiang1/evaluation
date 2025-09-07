    public void addAgent(String id, AgentInfo agentInfo) {
        agentInfoMap.put(id, agentInfo);
        if (this.tunnelClusterStore != null) {
            this.tunnelClusterStore.addAgent(id, new AgentClusterInfo(agentInfo, clientConnectHost, port), 60 * 60, TimeUnit.SECONDS);
        }
    }
