    public static ReadSheet match(ReadSheet readSheet, AnalysisContext analysisContext) {
        ReadWorkbookHolder readWorkbookHolder = analysisContext.readWorkbookHolder();
        if (readWorkbookHolder.getReadAll()) {
            return readSheet;
        }
        for (ReadSheet parameterReadSheet : readWorkbookHolder.getParameterSheetDataList()) {
            if (parameterReadSheet == null) {
                continue;
            }
            if (parameterReadSheet.getSheetNo() == null && parameterReadSheet.getSheetName() == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The first is read by default.");
                }
                parameterReadSheet.setSheetNo(0);
            }
            boolean match = (parameterReadSheet.getSheetNo() != null
                && parameterReadSheet.getSheetNo().equals(readSheet.getSheetNo()));
            if (!match) {
                String parameterSheetName = parameterReadSheet.getSheetName();
                if (!StringUtils.isEmpty(parameterSheetName)) {
                    boolean autoTrim = (parameterReadSheet.getAutoTrim() != null && parameterReadSheet.getAutoTrim())
                        || (parameterReadSheet.getAutoTrim() == null
                        && analysisContext.readWorkbookHolder().getGlobalConfiguration().getAutoTrim());
                    String sheetName = readSheet.getSheetName();
                    if (autoTrim) {
                        parameterSheetName = parameterSheetName.trim();
                        sheetName = sheetName.trim();
                    }
                    match = parameterSheetName.equals(sheetName);
                }
            }
            if (match) {
                readSheet.copyBasicParameter(parameterReadSheet);
                return readSheet;
            }
        }
        return null;
    }
