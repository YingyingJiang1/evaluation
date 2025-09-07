    private String getData(int rowIndex, ColumnDefine columnDefine) {
        return columnDefine.getHigh() <= rowIndex
                ? Constants.EMPTY_STRING
                : columnDefine.dataList.get(rowIndex);
    }
