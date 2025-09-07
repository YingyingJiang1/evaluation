    private static int getMemoryInfoHeight(Map<String, List<MemoryEntryVO>> memoryInfo) {
        int height = 1;
        for (List<MemoryEntryVO> memoryEntryVOS : memoryInfo.values()) {
            height += memoryEntryVOS.size();
        }
        return height;
    }
