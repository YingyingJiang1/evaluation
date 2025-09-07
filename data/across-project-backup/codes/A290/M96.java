    protected WriteCellData<?> convert(CellWriteHandlerContext cellWriteHandlerContext) {
        // This means that the user has defined the data.
        if (cellWriteHandlerContext.getOriginalFieldClass() == WriteCellData.class) {
            if (cellWriteHandlerContext.getOriginalValue() == null) {
                return new WriteCellData<>(CellDataTypeEnum.EMPTY);
            }
            WriteCellData<?> cellDataValue = (WriteCellData<?>)cellWriteHandlerContext.getOriginalValue();
            if (cellDataValue.getType() != null) {
                // Configuration information may not be read here
                fillProperty(cellDataValue, cellWriteHandlerContext.getExcelContentProperty());

                return cellDataValue;
            } else {
                if (cellDataValue.getData() == null) {
                    cellDataValue.setType(CellDataTypeEnum.EMPTY);
                    return cellDataValue;
                }
            }
            WriteCellData<?> cellDataReturn = doConvert(cellWriteHandlerContext);

            if (cellDataValue.getImageDataList() != null) {
                cellDataReturn.setImageDataList(cellDataValue.getImageDataList());
            }
            if (cellDataValue.getCommentData() != null) {
                cellDataReturn.setCommentData(cellDataValue.getCommentData());
            }
            if (cellDataValue.getHyperlinkData() != null) {
                cellDataReturn.setHyperlinkData(cellDataValue.getHyperlinkData());
            }
            // The formula information is subject to user input
            if (cellDataValue.getFormulaData() != null) {
                cellDataReturn.setFormulaData(cellDataValue.getFormulaData());
            }
            if (cellDataValue.getWriteCellStyle() != null) {
                cellDataReturn.setWriteCellStyle(cellDataValue.getWriteCellStyle());
            }
            return cellDataReturn;
        }
        return doConvert(cellWriteHandlerContext);
    }
