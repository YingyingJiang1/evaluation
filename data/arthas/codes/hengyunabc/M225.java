    @RequestMapping("/api/tunnelAgentInfo")
    @ResponseBody
    public Map<String, AgentClusterInfo> tunnelAgentIds(@RequestParam(value = "app", required = true) String appName,
            HttpServletRequest request, Model model) {
        if (!arthasProperties.isEnableDetailPages()) {
            throw new IllegalAccessError("not allow");
        }

        if (tunnelClusterStore != null) {
            Map<String, AgentClusterInfo> agentInfos = tunnelClusterStore.agentInfo(appName);

            return agentInfos;
        }

        return Collections.emptyMap();
    }
