    private String dealAnalysisCell(AnalysisCell analysisCell, String value, int rowIndex, int lastPrepareDataIndex,
        int length, Map<UniqueDataFlagKey, Set<Integer>> firstRowCache, StringBuilder preparedData) {
        if (analysisCell != null) {
            if (lastPrepareDataIndex == length) {
                analysisCell.getPrepareDataList().add(StringUtils.EMPTY);
            } else {
                analysisCell.getPrepareDataList().add(convertPrepareData(value.substring(lastPrepareDataIndex)));
                analysisCell.setOnlyOneVariable(Boolean.FALSE);
            }
            UniqueDataFlagKey uniqueDataFlag = uniqueDataFlag(writeContext.writeSheetHolder(),
                analysisCell.getPrefix());
            if (WriteTemplateAnalysisCellTypeEnum.COMMON.equals(analysisCell.getCellType())) {
                List<AnalysisCell> analysisCellList = templateAnalysisCache.computeIfAbsent(uniqueDataFlag,
                    key -> ListUtils.newArrayList());
                analysisCellList.add(analysisCell);
            } else {
                Set<Integer> uniqueFirstRowCache = firstRowCache.computeIfAbsent(uniqueDataFlag,
                    key -> new HashSet<>());

                if (!uniqueFirstRowCache.contains(rowIndex)) {
                    analysisCell.setFirstRow(Boolean.TRUE);
                    uniqueFirstRowCache.add(rowIndex);
                }

                List<AnalysisCell> collectionAnalysisCellList = templateCollectionAnalysisCache.computeIfAbsent(
                    uniqueDataFlag, key -> ListUtils.newArrayList());

                collectionAnalysisCellList.add(analysisCell);
            }
            return preparedData.toString();
        }
        return null;
    }
