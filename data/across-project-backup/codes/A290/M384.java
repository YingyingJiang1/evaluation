    private static Object doConvertToJavaObject(ReadCellData<?> cellData, Class<?> clazz,
        ExcelContentProperty contentProperty, Map<ConverterKey, Converter<?>> converterMap, AnalysisContext context,
        Integer rowIndex, Integer columnIndex) {
        Converter<?> converter = null;
        if (contentProperty != null) {
            converter = contentProperty.getConverter();
        }

        boolean canNotConverterEmpty = cellData.getType() == CellDataTypeEnum.EMPTY
            && !(converter instanceof NullableObjectConverter);
        if (canNotConverterEmpty) {
            return null;
        }

        if (converter == null) {
            converter = converterMap.get(ConverterKeyBuild.buildKey(clazz, cellData.getType()));
        }
        if (converter == null) {
            throw new ExcelDataConvertException(rowIndex, columnIndex, cellData, contentProperty,
                "Converter not found, convert " + cellData.getType() + " to " + clazz.getName());
        }

        try {
            return converter.convertToJavaData(new ReadConverterContext<>(cellData, contentProperty, context));
        } catch (Exception e) {
            throw new ExcelDataConvertException(rowIndex, columnIndex, cellData, contentProperty,
                "Convert data " + cellData + " to " + clazz + " error ", e);
        }
    }
