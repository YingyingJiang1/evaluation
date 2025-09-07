    public static Map<Integer, String> convertToStringMap(Map<Integer, ReadCellData<?>> cellDataMap,
        AnalysisContext context) {
        Map<Integer, String> stringMap = MapUtils.newHashMapWithExpectedSize(cellDataMap.size());
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        int index = 0;
        for (Map.Entry<Integer, ReadCellData<?>> entry : cellDataMap.entrySet()) {
            Integer key = entry.getKey();
            ReadCellData<?> cellData = entry.getValue();
            while (index < key) {
                stringMap.put(index, null);
                index++;
            }
            index++;
            if (cellData.getType() == CellDataTypeEnum.EMPTY) {
                stringMap.put(key, null);
                continue;
            }
            Converter<?> converter =
                readSheetHolder.converterMap().get(ConverterKeyBuild.buildKey(String.class, cellData.getType()));
            if (converter == null) {
                throw new ExcelDataConvertException(context.readRowHolder().getRowIndex(), key, cellData, null,
                    "Converter not found, convert " + cellData.getType() + " to String");
            }
            try {
                stringMap.put(key,
                    (String)(converter.convertToJavaData(new ReadConverterContext<>(cellData, null, context))));
            } catch (Exception e) {
                throw new ExcelDataConvertException(context.readRowHolder().getRowIndex(), key, cellData, null,
                    "Convert data " + cellData + " to String error ", e);
            }
        }
        return stringMap;
    }
