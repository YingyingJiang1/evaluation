    @Override
    public void endElement(XlsxReadContext xlsxReadContext, String name) {
        XlsxReadSheetHolder xlsxReadSheetHolder = xlsxReadContext.xlsxReadSheetHolder();
        RowTypeEnum rowType = MapUtils.isEmpty(xlsxReadSheetHolder.getCellMap()) ? RowTypeEnum.EMPTY : RowTypeEnum.DATA;
        // It's possible that all of the cells in the row are empty
        if (rowType == RowTypeEnum.DATA) {
            boolean hasData = false;
            for (Cell cell : xlsxReadSheetHolder.getCellMap().values()) {
                if (!(cell instanceof ReadCellData)) {
                    hasData = true;
                    break;
                }
                ReadCellData<?> readCellData = (ReadCellData<?>)cell;
                if (readCellData.getType() != CellDataTypeEnum.EMPTY) {
                    hasData = true;
                    break;
                }
            }
            if (!hasData) {
                rowType = RowTypeEnum.EMPTY;
            }
        }
        xlsxReadContext.readRowHolder(new ReadRowHolder(xlsxReadSheetHolder.getRowIndex(), rowType,
            xlsxReadSheetHolder.getGlobalConfiguration(), xlsxReadSheetHolder.getCellMap()));
        xlsxReadContext.analysisEventProcessor().endRow(xlsxReadContext);
        xlsxReadSheetHolder.setColumnIndex(null);
        xlsxReadSheetHolder.setCellMap(new LinkedHashMap<>());
    }
