    private WriteCellData<?> doConvert(CellWriteHandlerContext cellWriteHandlerContext) {
        ExcelContentProperty excelContentProperty = cellWriteHandlerContext.getExcelContentProperty();

        Converter<?> converter = null;
        if (excelContentProperty != null) {
            converter = excelContentProperty.getConverter();
        }
        if (converter == null) {
            // csv is converted to string by default
            if (writeContext.writeWorkbookHolder().getExcelType() == ExcelTypeEnum.CSV) {
                cellWriteHandlerContext.setTargetCellDataType(CellDataTypeEnum.STRING);
            }
            converter = writeContext.currentWriteHolder().converterMap().get(
                ConverterKeyBuild.buildKey(cellWriteHandlerContext.getOriginalFieldClass(),
                    cellWriteHandlerContext.getTargetCellDataType()));
        }
        if (cellWriteHandlerContext.getOriginalValue() == null && !(converter instanceof NullableObjectConverter)) {
            return new WriteCellData<>(CellDataTypeEnum.EMPTY);
        }
        if (converter == null) {
            throw new ExcelWriteDataConvertException(cellWriteHandlerContext,
                "Can not find 'Converter' support class " + cellWriteHandlerContext.getOriginalFieldClass()
                    .getSimpleName() + ".");
        }
        WriteCellData<?> cellData;
        try {
            cellData = ((Converter<Object>)converter).convertToExcelData(
                new WriteConverterContext<>(cellWriteHandlerContext.getOriginalValue(), excelContentProperty,
                    writeContext));
        } catch (Exception e) {
            throw new ExcelWriteDataConvertException(cellWriteHandlerContext,
                "Convert data:" + cellWriteHandlerContext.getOriginalValue() + " error, at row:"
                    + cellWriteHandlerContext.getRowIndex(), e);
        }
        if (cellData == null || cellData.getType() == null) {
            throw new ExcelWriteDataConvertException(cellWriteHandlerContext,
                "Convert data:" + cellWriteHandlerContext.getOriginalValue()
                    + " return is null or return type is null, at row:"
                    + cellWriteHandlerContext.getRowIndex());
        }
        return cellData;
    }
