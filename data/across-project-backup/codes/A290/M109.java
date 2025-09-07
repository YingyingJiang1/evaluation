    private String prepareData(Cell cell, int rowIndex, int columnIndex,
        Map<UniqueDataFlagKey, Set<Integer>> firstRowCache) {
        if (!CellType.STRING.equals(cell.getCellType())) {
            return null;
        }
        String value = cell.getStringCellValue();
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        StringBuilder preparedData = new StringBuilder();
        AnalysisCell analysisCell = null;

        int startIndex = 0;
        int length = value.length();
        int lastPrepareDataIndex = 0;
        out:
        while (startIndex < length) {
            int prefixIndex = value.indexOf(FILL_PREFIX, startIndex);
            if (prefixIndex < 0) {
                break;
            }
            if (prefixIndex != 0) {
                char prefixPrefixChar = value.charAt(prefixIndex - 1);
                if (prefixPrefixChar == IGNORE_CHAR) {
                    startIndex = prefixIndex + 1;
                    continue;
                }
            }
            int suffixIndex = -1;
            while (suffixIndex == -1 && startIndex < length) {
                suffixIndex = value.indexOf(FILL_SUFFIX, startIndex + 1);
                if (suffixIndex < 0) {
                    break out;
                }
                startIndex = suffixIndex + 1;
                char prefixSuffixChar = value.charAt(suffixIndex - 1);
                if (prefixSuffixChar == IGNORE_CHAR) {
                    suffixIndex = -1;
                }
            }
            if (analysisCell == null) {
                analysisCell = initAnalysisCell(rowIndex, columnIndex);
            }
            String variable = value.substring(prefixIndex + 1, suffixIndex);
            if (StringUtils.isEmpty(variable)) {
                continue;
            }
            int collectPrefixIndex = variable.indexOf(COLLECTION_PREFIX);
            if (collectPrefixIndex > -1) {
                if (collectPrefixIndex != 0) {
                    analysisCell.setPrefix(variable.substring(0, collectPrefixIndex));
                }
                variable = variable.substring(collectPrefixIndex + 1);
                if (StringUtils.isEmpty(variable)) {
                    continue;
                }
                analysisCell.setCellType(WriteTemplateAnalysisCellTypeEnum.COLLECTION);
            }
            analysisCell.getVariableList().add(variable);
            if (lastPrepareDataIndex == prefixIndex) {
                analysisCell.getPrepareDataList().add(StringUtils.EMPTY);
                // fix https://github.com/alibaba/easyexcel/issues/2035
                if (lastPrepareDataIndex != 0) {
                    analysisCell.setOnlyOneVariable(Boolean.FALSE);
                }
            } else {
                String data = convertPrepareData(value.substring(lastPrepareDataIndex, prefixIndex));
                preparedData.append(data);
                analysisCell.getPrepareDataList().add(data);
                analysisCell.setOnlyOneVariable(Boolean.FALSE);
            }
            lastPrepareDataIndex = suffixIndex + 1;
        }
        // fix https://github.com/alibaba/easyexcel/issues/1552
        // When read template, XLSX data may be in `is` labels, and set the time set in `v` label, lead to can't set
        // up successfully, so all data format to empty first.
        if (analysisCell != null && CollectionUtils.isNotEmpty(analysisCell.getVariableList())) {
            cell.setBlank();
        }
        return dealAnalysisCell(analysisCell, value, rowIndex, lastPrepareDataIndex, length, firstRowCache,
            preparedData);
    }
