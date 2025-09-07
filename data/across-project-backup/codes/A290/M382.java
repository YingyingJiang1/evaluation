    public static Object convertToJavaObject(ReadCellData<?> cellData, Field field, Class<?> clazz,
        Class<?> classGeneric, ExcelContentProperty contentProperty, Map<ConverterKey, Converter<?>> converterMap,
        AnalysisContext context, Integer rowIndex, Integer columnIndex) {
        if (clazz == null) {
            if (field == null) {
                clazz = String.class;
            } else {
                clazz = field.getType();
            }
        }
        if (clazz == CellData.class || clazz == ReadCellData.class) {
            ReadCellData<Object> cellDataReturn = cellData.clone();
            cellDataReturn.setData(
                doConvertToJavaObject(cellData, getClassGeneric(field, classGeneric), contentProperty,
                    converterMap, context, rowIndex, columnIndex));
            return cellDataReturn;
        }
        return doConvertToJavaObject(cellData, clazz, contentProperty, converterMap, context, rowIndex,
            columnIndex);
    }
