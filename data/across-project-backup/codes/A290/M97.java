    private void fillProperty(WriteCellData<?> cellDataValue, ExcelContentProperty excelContentProperty) {
        switch (cellDataValue.getType()) {
            case DATE:
                String dateFormat = null;
                if (excelContentProperty != null && excelContentProperty.getDateTimeFormatProperty() != null) {
                    dateFormat = excelContentProperty.getDateTimeFormatProperty().getFormat();
                }
                WorkBookUtil.fillDataFormat(cellDataValue, dateFormat, DateUtils.defaultDateFormat);
                return;
            case NUMBER:
                String numberFormat = null;
                if (excelContentProperty != null && excelContentProperty.getNumberFormatProperty() != null) {
                    numberFormat = excelContentProperty.getNumberFormatProperty().getFormat();
                }
                WorkBookUtil.fillDataFormat(cellDataValue, numberFormat, null);
                return;
            default:
                return;
        }
    }
