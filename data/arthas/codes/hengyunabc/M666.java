    private static void addBufferPoolMemoryInfo(Map<String, List<MemoryEntryVO>> memoryInfoMap) {
        try {
            List<MemoryEntryVO> bufferPoolMemEntries = new ArrayList<MemoryEntryVO>();
            @SuppressWarnings("rawtypes")
            Class bufferPoolMXBeanClass = Class.forName("java.lang.management.BufferPoolMXBean");
            @SuppressWarnings("unchecked")
            List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(bufferPoolMXBeanClass);
            for (BufferPoolMXBean mbean : bufferPoolMXBeans) {
                long used = mbean.getMemoryUsed();
                long total = mbean.getTotalCapacity();
                bufferPoolMemEntries
                        .add(new MemoryEntryVO(TYPE_BUFFER_POOL, mbean.getName(), used, total, Long.MIN_VALUE));
            }
            memoryInfoMap.put(TYPE_BUFFER_POOL, bufferPoolMemEntries);
        } catch (ClassNotFoundException e) {
            // ignore
        }
    }
