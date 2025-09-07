    private void initSheet(WriteTypeEnum writeType) {
        SheetWriteHandlerContext sheetWriteHandlerContext = WriteHandlerUtils.createSheetWriteHandlerContext(this);
        WriteHandlerUtils.beforeSheetCreate(sheetWriteHandlerContext);
        Sheet currentSheet;
        try {
            if (writeSheetHolder.getSheetNo() != null) {
                // When the add default sort order of appearance
                if (WriteTypeEnum.ADD.equals(writeType) && writeWorkbookHolder.getTempTemplateInputStream() == null) {
                    currentSheet = createSheet();
                } else {
                    currentSheet = writeWorkbookHolder.getWorkbook().getSheetAt(writeSheetHolder.getSheetNo());
                    writeSheetHolder
                        .setCachedSheet(
                            writeWorkbookHolder.getCachedWorkbook().getSheetAt(writeSheetHolder.getSheetNo()));
                }
            } else {
                // sheet name must not null
                currentSheet = writeWorkbookHolder.getWorkbook().getSheet(writeSheetHolder.getSheetName());
                writeSheetHolder
                    .setCachedSheet(writeWorkbookHolder.getCachedWorkbook().getSheet(writeSheetHolder.getSheetName()));
            }
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains(NO_SHEETS)) {
                currentSheet = createSheet();
            } else {
                throw e;
            }
        }
        if (currentSheet == null) {
            currentSheet = createSheet();
        }
        writeSheetHolder.setSheet(currentSheet);
        WriteHandlerUtils.afterSheetCreate(sheetWriteHandlerContext);
        if (WriteTypeEnum.ADD.equals(writeType)) {
            // Initialization head
            initHead(writeSheetHolder.excelWriteHeadProperty());
        }
        writeWorkbookHolder.getHasBeenInitializedSheetIndexMap().put(writeSheetHolder.getSheetNo(), writeSheetHolder);
        writeWorkbookHolder.getHasBeenInitializedSheetNameMap().put(writeSheetHolder.getSheetName(), writeSheetHolder);
    }
