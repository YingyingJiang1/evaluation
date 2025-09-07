    private ReadCellData convertReadCellData(ReadCellData<?> cellData, ReadDefaultReturnEnum readDefaultReturn,
        ReadSheetHolder readSheetHolder, AnalysisContext context, Integer columnIndex) {
        Class<?> classGeneric;
        switch (cellData.getType()) {
            case STRING:
            case DIRECT_STRING:
            case ERROR:
            case EMPTY:
                classGeneric = String.class;
                break;
            case BOOLEAN:
                classGeneric = Boolean.class;
                break;
            case NUMBER:
                DataFormatData dataFormatData = cellData.getDataFormatData();
                if (dataFormatData != null && DateUtils.isADateFormat(dataFormatData.getIndex(),
                    dataFormatData.getFormat())) {
                    classGeneric = LocalDateTime.class;
                } else {
                    classGeneric = BigDecimal.class;
                }
                break;
            default:
                classGeneric = ConverterUtils.defaultClassGeneric;
                break;
        }

        return (ReadCellData)ConverterUtils.convertToJavaObject(cellData, null, ReadCellData.class,
            classGeneric, null, readSheetHolder.converterMap(), context, context.readRowHolder().getRowIndex(),
            columnIndex);
    }
