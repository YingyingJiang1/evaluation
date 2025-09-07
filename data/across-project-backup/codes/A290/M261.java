    private void dealRecord(CSVRecord record, int rowIndex) {
        Map<Integer, Cell> cellMap = new LinkedHashMap<>();
        Iterator<String> cellIterator = record.iterator();
        int columnIndex = 0;
        Boolean autoTrim = csvReadContext.currentReadHolder().globalConfiguration().getAutoTrim();
        while (cellIterator.hasNext()) {
            String cellString = cellIterator.next();
            ReadCellData<String> readCellData = new ReadCellData<>();
            readCellData.setRowIndex(rowIndex);
            readCellData.setColumnIndex(columnIndex);

            // csv is an empty string of whether <code>,,</code> is read or <code>,"",</code>
            if (StringUtils.isNotBlank(cellString)) {
                readCellData.setType(CellDataTypeEnum.STRING);
                readCellData.setStringValue(autoTrim ? cellString.trim() : cellString);
            } else {
                readCellData.setType(CellDataTypeEnum.EMPTY);
            }
            cellMap.put(columnIndex++, readCellData);
        }

        RowTypeEnum rowType = MapUtils.isEmpty(cellMap) ? RowTypeEnum.EMPTY : RowTypeEnum.DATA;
        ReadRowHolder readRowHolder = new ReadRowHolder(rowIndex, rowType,
            csvReadContext.readWorkbookHolder().getGlobalConfiguration(), cellMap);
        csvReadContext.readRowHolder(readRowHolder);

        csvReadContext.csvReadSheetHolder().setCellMap(cellMap);
        csvReadContext.csvReadSheetHolder().setRowIndex(rowIndex);
        csvReadContext.analysisEventProcessor().endRow(csvReadContext);
    }
