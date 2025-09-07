    static String drawMemoryInfoAndGcInfo(Map<String, List<MemoryEntryVO>> memoryInfo, List<GcInfoVO> gcInfos, int width, int height) {
        TableElement table = new TableElement(1, 1);
        TableElement memoryInfoTable = MemoryView.drawMemoryInfo(memoryInfo);
        TableElement gcInfoTable = drawGcInfo(gcInfos);
        table.row(memoryInfoTable, gcInfoTable);
        return RenderUtil.render(table, width, height);
    }
