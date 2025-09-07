    private Map<String, Object> getMemoryUsageInfo(String name, MemoryUsage heapMemoryUsage) {
        Map<String, Object> memoryInfo = new LinkedHashMap<String, Object>();
        memoryInfo.put("name", name);
        memoryInfo.put("init", heapMemoryUsage.getInit());
        memoryInfo.put("used", heapMemoryUsage.getUsed());
        memoryInfo.put("committed", heapMemoryUsage.getCommitted());
        memoryInfo.put("max", heapMemoryUsage.getMax());
        return memoryInfo;
    }
