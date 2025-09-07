    private String buildCellValue(CsvCell csvCell) {
        switch (csvCell.getCellType()) {
            case STRING:
            case ERROR:
                return csvCell.getStringCellValue();
            case NUMERIC:
                Short dataFormat = null;
                String dataFormatString = null;
                if (csvCell.getCellStyle() != null) {
                    dataFormat = csvCell.getCellStyle().getDataFormat();
                    dataFormatString = csvCell.getCellStyle().getDataFormatString();
                }
                if (csvCell.getNumericCellType() == NumericCellTypeEnum.DATE) {
                    if (csvCell.getDateValue() == null) {
                        return null;
                    }
                    // date
                    if (dataFormat == null) {
                        dataFormatString = DateUtils.defaultDateFormat;
                        dataFormat = csvWorkbook.createDataFormat().getFormat(dataFormatString);
                    }
                    if (dataFormatString == null) {
                        dataFormatString = csvWorkbook.createDataFormat().getFormat(dataFormat);
                    }
                    return NumberDataFormatterUtils.format(BigDecimal.valueOf(
                            DateUtil.getExcelDate(csvCell.getDateValue(), csvWorkbook.getUse1904windowing())),
                        dataFormat, dataFormatString, csvWorkbook.getUse1904windowing(), csvWorkbook.getLocale(),
                        csvWorkbook.getUseScientificFormat());
                } else {
                    if (csvCell.getNumberValue() == null) {
                        return null;
                    }
                    //number
                    if (dataFormat == null) {
                        dataFormat = BuiltinFormats.GENERAL;
                        dataFormatString = csvWorkbook.createDataFormat().getFormat(dataFormat);
                    }
                    if (dataFormatString == null) {
                        dataFormatString = csvWorkbook.createDataFormat().getFormat(dataFormat);
                    }
                    return NumberDataFormatterUtils.format(csvCell.getNumberValue(), dataFormat, dataFormatString,
                        csvWorkbook.getUse1904windowing(), csvWorkbook.getLocale(),
                        csvWorkbook.getUseScientificFormat());
                }
            case BOOLEAN:
                return csvCell.getBooleanValue().toString();
            case BLANK:
                return StringUtils.EMPTY;
            default:
                return null;
        }
    }
