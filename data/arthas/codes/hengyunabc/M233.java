            @Override
            public void run() {
                agentInfoMap.entrySet().removeIf(e -> !e.getValue().getChannelHandlerContext().channel().isActive());
                clientConnectionInfoMap.entrySet()
                        .removeIf(e -> !e.getValue().getChannelHandlerContext().channel().isActive());
                
                // 更新集群key信息
                if (tunnelClusterStore != null && clientConnectHost != null) {
                    try {
                        for (Entry<String, AgentInfo> entry : agentInfoMap.entrySet()) {
                            tunnelClusterStore.addAgent(entry.getKey(), new AgentClusterInfo(entry.getValue(), clientConnectHost, port), 60 * 60, TimeUnit.SECONDS);
                        }
                    } catch (Throwable t) {
                        logger.error("update tunnel info error", t);
                    }
                }
            }
