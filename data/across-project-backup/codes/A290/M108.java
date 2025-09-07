    private List<AnalysisCell> readTemplateData(Map<UniqueDataFlagKey, List<AnalysisCell>> analysisCache) {
        List<AnalysisCell> analysisCellList = analysisCache.get(currentUniqueDataFlag);
        if (analysisCellList != null) {
            return analysisCellList;
        }
        Sheet sheet = writeContext.writeSheetHolder().getCachedSheet();
        Map<UniqueDataFlagKey, Set<Integer>> firstRowCache = MapUtils.newHashMapWithExpectedSize(8);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                String preparedData = prepareData(cell, i, j, firstRowCache);
                // Prevent empty data from not being replaced
                if (preparedData != null) {
                    cell.setCellValue(preparedData);
                }
            }
        }
        return analysisCache.get(currentUniqueDataFlag);
    }
