    @Override
    public void processRecord(XlsReadContext xlsReadContext, Record record) {
        BOFRecord br = (BOFRecord) record;
        XlsReadWorkbookHolder xlsReadWorkbookHolder = xlsReadContext.xlsReadWorkbookHolder();
        if (br.getType() == BOFRecord.TYPE_WORKBOOK) {
            xlsReadWorkbookHolder.setReadSheetIndex(null);
            xlsReadWorkbookHolder.setIgnoreRecord(Boolean.FALSE);
            return;
        }
        if (br.getType() != BOFRecord.TYPE_WORKSHEET) {
            return;
        }
        // Init read sheet Data
        initReadSheetDataList(xlsReadWorkbookHolder);
        Integer readSheetIndex = xlsReadWorkbookHolder.getReadSheetIndex();
        if (readSheetIndex == null) {
            readSheetIndex = 0;
            xlsReadWorkbookHolder.setReadSheetIndex(readSheetIndex);
        }
        ReadSheet actualReadSheet = xlsReadWorkbookHolder.getActualSheetDataList().get(readSheetIndex);
        assert actualReadSheet != null : "Can't find the sheet.";
        // Copy the parameter to the current sheet
        ReadSheet readSheet = SheetUtils.match(actualReadSheet, xlsReadContext);
        if (readSheet != null) {
            xlsReadContext.currentSheet(readSheet);
            xlsReadContext.xlsReadWorkbookHolder().setIgnoreRecord(Boolean.FALSE);
        } else {
            xlsReadContext.xlsReadWorkbookHolder().setIgnoreRecord(Boolean.TRUE);
        }
        xlsReadContext.xlsReadWorkbookHolder().setCurrentSheetStopped(Boolean.FALSE);
        // Go read the next one
        xlsReadWorkbookHolder.setReadSheetIndex(xlsReadWorkbookHolder.getReadSheetIndex() + 1);
    }
