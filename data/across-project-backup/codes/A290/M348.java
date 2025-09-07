    public static void combineExcelContentProperty(ExcelContentProperty combineExcelContentProperty,
        ExcelContentProperty excelContentProperty) {
        if (excelContentProperty == null) {
            return;
        }
        if (excelContentProperty.getField() != null) {
            combineExcelContentProperty.setField(excelContentProperty.getField());
        }
        if (excelContentProperty.getConverter() != null) {
            combineExcelContentProperty.setConverter(excelContentProperty.getConverter());
        }
        if (excelContentProperty.getDateTimeFormatProperty() != null) {
            combineExcelContentProperty.setDateTimeFormatProperty(excelContentProperty.getDateTimeFormatProperty());
        }
        if (excelContentProperty.getNumberFormatProperty() != null) {
            combineExcelContentProperty.setNumberFormatProperty(excelContentProperty.getNumberFormatProperty());
        }
        if (excelContentProperty.getContentStyleProperty() != null) {
            combineExcelContentProperty.setContentStyleProperty(excelContentProperty.getContentStyleProperty());
        }
        if (excelContentProperty.getContentFontProperty() != null) {
            combineExcelContentProperty.setContentFontProperty(excelContentProperty.getContentFontProperty());
        }
    }
