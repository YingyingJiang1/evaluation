    @Override
    public void endElement(XlsxReadContext xlsxReadContext, String name) {
        XlsxReadSheetHolder xlsxReadSheetHolder = xlsxReadContext.xlsxReadSheetHolder();
        ReadCellData<?> tempCellData = xlsxReadSheetHolder.getTempCellData();
        StringBuilder tempData = xlsxReadSheetHolder.getTempData();
        String tempDataString = tempData.toString();
        CellDataTypeEnum oldType = tempCellData.getType();
        switch (oldType) {
            case STRING:
                // In some cases, although cell type is a string, it may be an empty tag
                if (StringUtils.isEmpty(tempDataString)) {
                    break;
                }
                String stringValue = xlsxReadContext.readWorkbookHolder().getReadCache().get(
                    Integer.valueOf(tempDataString));
                tempCellData.setStringValue(stringValue);
                break;
            case DIRECT_STRING:
            case ERROR:
                tempCellData.setStringValue(tempDataString);
                tempCellData.setType(CellDataTypeEnum.STRING);
                break;
            case BOOLEAN:
                if (StringUtils.isEmpty(tempDataString)) {
                    tempCellData.setType(CellDataTypeEnum.EMPTY);
                    break;
                }
                tempCellData.setBooleanValue(BooleanUtils.valueOf(tempData.toString()));
                break;
            case NUMBER:
            case EMPTY:
                if (StringUtils.isEmpty(tempDataString)) {
                    tempCellData.setType(CellDataTypeEnum.EMPTY);
                    break;
                }
                tempCellData.setType(CellDataTypeEnum.NUMBER);
                tempCellData.setOriginalNumberValue(new BigDecimal(tempDataString));
                tempCellData.setNumberValue(
                    tempCellData.getOriginalNumberValue().round(EasyExcelConstants.EXCEL_MATH_CONTEXT));
                break;
            default:
                throw new IllegalStateException("Cannot set values now");
        }

        if (tempCellData.getStringValue() != null
            && xlsxReadContext.currentReadHolder().globalConfiguration().getAutoTrim()) {
            tempCellData.setStringValue(tempCellData.getStringValue().trim());
        }

        tempCellData.checkEmpty();
        tempCellData.setRowIndex(xlsxReadSheetHolder.getRowIndex());
        tempCellData.setColumnIndex(xlsxReadSheetHolder.getColumnIndex());
        xlsxReadSheetHolder.getCellMap().put(xlsxReadSheetHolder.getColumnIndex(), tempCellData);
    }
