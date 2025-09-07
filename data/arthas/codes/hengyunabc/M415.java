    public TableView addRow(Object... columnDataArray) {
        if (null == columnDataArray) {
            return this;
        }

        for (int index = 0; index < columnDefineArray.length; index++) {
            final ColumnDefine columnDefine = columnDefineArray[index];
            if (index < columnDataArray.length
                    && null != columnDataArray[index]) {
                columnDefine.dataList.add(StringUtils.replace(columnDataArray[index].toString(), "\t", "    "));
            } else {
                columnDefine.dataList.add(Constants.EMPTY_STRING);
            }
        }

        return this;
    }
