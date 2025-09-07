    @Override
    public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
        if (writeWorkbookHolder == null || writeWorkbookHolder.getWorkbook() == null) {
            return;
        }
        if (!(writeWorkbookHolder.getWorkbook() instanceof SXSSFWorkbook)) {
            return;
        }

        Map<Integer, WriteSheetHolder> writeSheetHolderMap = writeWorkbookHolder.getHasBeenInitializedSheetIndexMap();
        if (MapUtils.isEmpty(writeSheetHolderMap)) {
            return;
        }
        for (WriteSheetHolder writeSheetHolder : writeSheetHolderMap.values()) {
            if (writeSheetHolder.getSheet() == null || !(writeSheetHolder.getSheet() instanceof SXSSFSheet)) {
                continue;
            }
            SXSSFSheet sxssfSheet = ((SXSSFSheet)writeSheetHolder.getSheet());
            XSSFSheet xssfSheet;
            try {
                xssfSheet = (XSSFSheet)XSSF_SHEET_FIELD.get(sxssfSheet);
            } catch (IllegalAccessException e) {
                log.debug("Can not found _sh.", e);
                continue;
            }
            if (xssfSheet == null) {
                continue;
            }
            CTWorksheet ctWorksheet = xssfSheet.getCTWorksheet();
            if (ctWorksheet == null) {
                continue;
            }
            int headSize = 0;
            if (MapUtils.isNotEmpty(writeSheetHolder.getExcelWriteHeadProperty().getHeadMap())) {
                headSize = writeSheetHolder.getExcelWriteHeadProperty().getHeadMap().size();
                if (headSize > 0) {
                    headSize--;
                }
            }
            Integer lastRowIndex = writeSheetHolder.getLastRowIndex();
            if (lastRowIndex == null) {
                lastRowIndex = 0;
            }

            ctWorksheet.getDimension().setRef(
                "A1:" + CellReference.convertNumToColString(headSize) + (lastRowIndex + 1));
        }
    }
