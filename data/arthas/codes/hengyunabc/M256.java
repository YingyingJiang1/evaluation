    @Override
    public void removeAgent(String agentId) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.getOperations().delete(prefix + agentId);
    }
