    public static RichTextString buildRichTextString(WriteWorkbookHolder writeWorkbookHolder,
        RichTextStringData richTextStringData) {
        if (richTextStringData == null) {
            return null;
        }
        RichTextString richTextString;
        if (writeWorkbookHolder.getExcelType() == ExcelTypeEnum.XLSX) {
            richTextString = new XSSFRichTextString(richTextStringData.getTextString());
        } else {
            richTextString = new HSSFRichTextString(richTextStringData.getTextString());
        }
        if (richTextStringData.getWriteFont() != null) {
            richTextString.applyFont(writeWorkbookHolder.createFont(richTextStringData.getWriteFont(), null, true));
        }
        if (CollectionUtils.isNotEmpty(richTextStringData.getIntervalFontList())) {
            for (IntervalFont intervalFont : richTextStringData.getIntervalFontList()) {
                richTextString.applyFont(intervalFont.getStartIndex(), intervalFont.getEndIndex(),
                    writeWorkbookHolder.createFont(intervalFont.getWriteFont(), null, true));
            }
        }
        return richTextString;
    }
