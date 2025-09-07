    private void doFill(List<AnalysisCell> analysisCellList, Object oneRowData, FillConfig fillConfig,
        Integer relativeRowIndex) {
        if (CollectionUtils.isEmpty(analysisCellList) || oneRowData == null) {
            return;
        }
        Map dataMap;
        if (oneRowData instanceof Map) {
            dataMap = (Map)oneRowData;
        } else {
            dataMap = BeanMapUtils.create(oneRowData);
        }
        Set<String> dataKeySet = new HashSet<>(dataMap.keySet());

        RowWriteHandlerContext rowWriteHandlerContext = WriteHandlerUtils.createRowWriteHandlerContext(writeContext,
            null, relativeRowIndex, Boolean.FALSE);

        for (AnalysisCell analysisCell : analysisCellList) {
            CellWriteHandlerContext cellWriteHandlerContext = WriteHandlerUtils.createCellWriteHandlerContext(
                writeContext, null, analysisCell.getRowIndex(), null, analysisCell.getColumnIndex(),
                relativeRowIndex, Boolean.FALSE, ExcelContentProperty.EMPTY);

            if (analysisCell.getOnlyOneVariable()) {
                String variable = analysisCell.getVariableList().get(0);
                if (!dataKeySet.contains(variable)) {
                    continue;
                }
                Object value = dataMap.get(variable);
                ExcelContentProperty excelContentProperty = ClassUtils.declaredExcelContentProperty(dataMap,
                    writeContext.currentWriteHolder().excelWriteHeadProperty().getHeadClazz(), variable,
                    writeContext.currentWriteHolder());
                cellWriteHandlerContext.setExcelContentProperty(excelContentProperty);

                createCell(analysisCell, fillConfig, cellWriteHandlerContext, rowWriteHandlerContext);
                cellWriteHandlerContext.setOriginalValue(value);
                cellWriteHandlerContext.setOriginalFieldClass(FieldUtils.getFieldClass(dataMap, variable, value));

                converterAndSet(cellWriteHandlerContext);
                WriteCellData<?> cellData = cellWriteHandlerContext.getFirstCellData();

                // Restyle
                if (fillConfig.getAutoStyle()) {
                    Optional.ofNullable(collectionFieldStyleCache.get(currentUniqueDataFlag))
                        .map(collectionFieldStyleMap -> collectionFieldStyleMap.get(analysisCell))
                        .ifPresent(cellData::setOriginCellStyle);
                }
            } else {
                StringBuilder cellValueBuild = new StringBuilder();
                int index = 0;
                List<WriteCellData<?>> cellDataList = new ArrayList<>();

                cellWriteHandlerContext.setExcelContentProperty(ExcelContentProperty.EMPTY);
                cellWriteHandlerContext.setIgnoreFillStyle(Boolean.TRUE);

                createCell(analysisCell, fillConfig, cellWriteHandlerContext, rowWriteHandlerContext);
                Cell cell = cellWriteHandlerContext.getCell();

                for (String variable : analysisCell.getVariableList()) {
                    cellValueBuild.append(analysisCell.getPrepareDataList().get(index++));
                    if (!dataKeySet.contains(variable)) {
                        continue;
                    }
                    Object value = dataMap.get(variable);
                    ExcelContentProperty excelContentProperty = ClassUtils.declaredExcelContentProperty(dataMap,
                        writeContext.currentWriteHolder().excelWriteHeadProperty().getHeadClazz(), variable,
                        writeContext.currentWriteHolder());
                    cellWriteHandlerContext.setOriginalValue(value);
                    cellWriteHandlerContext.setOriginalFieldClass(FieldUtils.getFieldClass(dataMap, variable, value));
                    cellWriteHandlerContext.setExcelContentProperty(excelContentProperty);
                    cellWriteHandlerContext.setTargetCellDataType(CellDataTypeEnum.STRING);

                    WriteCellData<?> cellData = convert(cellWriteHandlerContext);
                    cellDataList.add(cellData);

                    CellDataTypeEnum type = cellData.getType();
                    if (type != null) {
                        switch (type) {
                            case STRING:
                                cellValueBuild.append(cellData.getStringValue());
                                break;
                            case BOOLEAN:
                                cellValueBuild.append(cellData.getBooleanValue());
                                break;
                            case NUMBER:
                                cellValueBuild.append(cellData.getNumberValue());
                                break;
                            default:
                                break;
                        }
                    }
                }
                cellValueBuild.append(analysisCell.getPrepareDataList().get(index));
                cell.setCellValue(cellValueBuild.toString());
                cellWriteHandlerContext.setCellDataList(cellDataList);
                if (CollectionUtils.isNotEmpty(cellDataList)) {
                    cellWriteHandlerContext.setFirstCellData(cellDataList.get(0));
                }

                // Restyle
                if (fillConfig.getAutoStyle()) {
                    Optional.ofNullable(collectionFieldStyleCache.get(currentUniqueDataFlag))
                        .map(collectionFieldStyleMap -> collectionFieldStyleMap.get(analysisCell))
                        .ifPresent(cell::setCellStyle);
                }
            }
            WriteHandlerUtils.afterCellDispose(cellWriteHandlerContext);
        }

        // In the case of the fill line may be called many times
        if (rowWriteHandlerContext.getRow() != null) {
            WriteHandlerUtils.afterRowDispose(rowWriteHandlerContext);
        }
    }
