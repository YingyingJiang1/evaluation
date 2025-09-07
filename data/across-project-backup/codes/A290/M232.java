    private boolean selectSheetFromCache(WriteSheet writeSheet) {
        writeSheetHolder = null;
        Integer sheetNo = writeSheet.getSheetNo();
        if (sheetNo == null && StringUtils.isEmpty(writeSheet.getSheetName())) {
            sheetNo = 0;
        }
        if (sheetNo != null) {
            writeSheetHolder = writeWorkbookHolder.getHasBeenInitializedSheetIndexMap().get(sheetNo);
        }
        if (writeSheetHolder == null && !StringUtils.isEmpty(writeSheet.getSheetName())) {
            writeSheetHolder = writeWorkbookHolder.getHasBeenInitializedSheetNameMap().get(writeSheet.getSheetName());
        }
        if (writeSheetHolder == null) {
            return false;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sheet:{},{} is already existed", writeSheet.getSheetNo(), writeSheet.getSheetName());
        }
        writeSheetHolder.setNewInitialization(Boolean.FALSE);
        writeTableHolder = null;
        currentWriteHolder = writeSheetHolder;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeSheetHolder");
        }
        return true;
    }
