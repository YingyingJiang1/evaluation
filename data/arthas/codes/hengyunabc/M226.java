    @RequestMapping("/api/tunnelAgents")
    @ResponseBody
    public Map<String, Object> tunnelAgentIds(@RequestParam(value = "agentId", required = true) String agentId) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean success = false;
        try {
            AgentClusterInfo info = tunnelClusterStore.findAgent(agentId);
            if (info != null) {
                success = true;
            }
        } catch (Throwable e) {
            logger.error("try to find agentId error, id: {}", agentId, e);
        }
        result.put("success", success);
        return result;
    }
