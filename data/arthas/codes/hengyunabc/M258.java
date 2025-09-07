    @Override
    public Map<String, AgentClusterInfo> agentInfo(String appName) {
        try {

            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

            String prefixWithAppName = prefix + appName + "_";

            ArrayList<String> keys = new ArrayList<>(opsForValue.getOperations().keys(prefixWithAppName + "*"));

            List<String> values = opsForValue.getOperations().opsForValue().multiGet(keys);

            Map<String, AgentClusterInfo> result = new HashMap<>();

            Iterator<String> iterator = values.iterator();

            for (String key : keys) {
                String infoStr = iterator.next();
                AgentClusterInfo info = MAPPER.readValue(infoStr, AgentClusterInfo.class);
                String agentId = key.substring(prefix.length());
                result.put(agentId, info);
            }

            return result;
        } catch (Throwable e) {
            logger.error("try to query agentInfo error. appName:{}", appName, e);
            throw new RuntimeException(e);
        }
    }
