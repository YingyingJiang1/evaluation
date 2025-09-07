    @Override
    public void addAgent(String agentId, AgentClusterInfo info, long timeout, TimeUnit timeUnit) {
        try {
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            String infoStr = MAPPER.writeValueAsString(info);
            opsForValue.set(prefix + agentId, infoStr, timeout, timeUnit);
        } catch (Throwable e) {
            logger.error("try to add agentInfo error. agentId:{}", agentId, e);
            throw new RuntimeException(e);
        }
    }
