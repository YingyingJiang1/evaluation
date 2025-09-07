    @RequestMapping(value = "/api/cluster/findHost")
    @ResponseBody
    public String execute(@RequestParam(value = "agentId", required = true) String agentId) {
        TunnelClusterStore tunnelClusterStore = tunnelServer.getTunnelClusterStore();

        String host = null;
        if (tunnelClusterStore != null) {
            AgentClusterInfo info = tunnelClusterStore.findAgent(agentId);
            host = info.getClientConnectHost();
        }

        if (host == null) {
            host = "";
        }

        logger.info("arthas cluster findHost, agentId: {}, host: {}", agentId, host);

        return host;
    }
