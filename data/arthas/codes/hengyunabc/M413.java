    private int getTableHigh() {
        int tableHigh = 0;
        for (ColumnDefine columnDefine : columnDefineArray) {
            tableHigh = max(tableHigh, columnDefine.getHigh());
        }
        return tableHigh;
    }
