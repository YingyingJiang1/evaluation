    static TableElement drawMemoryInfo(Map<String, List<MemoryEntryVO>> memoryInfo) {
        TableElement table = new TableElement(3, 1, 1, 1, 1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.fg(Color.black).bg(Color.white)).add("Memory",
                "used", "total", "max", "usage"));
        List<MemoryEntryVO> heapMemoryEntries = memoryInfo.get(MemoryEntryVO.TYPE_HEAP);
        //heap memory
        for (MemoryEntryVO memoryEntryVO : heapMemoryEntries) {
            if (MemoryEntryVO.TYPE_HEAP.equals(memoryEntryVO.getName())) {
                new MemoryEntry(memoryEntryVO).addTableRow(table, Decoration.bold.bold());
            } else {
                new MemoryEntry(memoryEntryVO).addTableRow(table);
            }
        }

        //non-heap memory
        List<MemoryEntryVO> nonheapMemoryEntries = memoryInfo.get(MemoryEntryVO.TYPE_NON_HEAP);
        for (MemoryEntryVO memoryEntryVO : nonheapMemoryEntries) {
            if (MemoryEntryVO.TYPE_NON_HEAP.equals(memoryEntryVO.getName())) {
                new MemoryEntry(memoryEntryVO).addTableRow(table, Decoration.bold.bold());
            } else {
                new MemoryEntry(memoryEntryVO).addTableRow(table);
            }
        }

        //buffer-pool
        List<MemoryEntryVO> bufferPoolMemoryEntries = memoryInfo.get(MemoryEntryVO.TYPE_BUFFER_POOL);
        if (bufferPoolMemoryEntries != null) {
            for (MemoryEntryVO memoryEntryVO : bufferPoolMemoryEntries) {
                new MemoryEntry(memoryEntryVO).addTableRow(table);
            }
        }
        return table;
    }
