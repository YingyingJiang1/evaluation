    @Override
    public AgentClusterInfo findAgent(String agentId) {
        try {
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            String infoStr = opsForValue.get(prefix + agentId);
            if (infoStr == null) {
                throw new IllegalArgumentException("can not find info for agentId: " + agentId);
            }
            AgentClusterInfo info = MAPPER.readValue(infoStr, AgentClusterInfo.class);
            return info;
        } catch (Throwable e) {
            logger.error("try to read agentInfo error. agentId:{}", agentId, e);
            throw new RuntimeException(e);
        }
    }
