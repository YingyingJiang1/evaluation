    private void copyTemplate() throws IOException {
        if (writeWorkbook.getTemplateFile() == null && writeWorkbook.getTemplateInputStream() == null) {
            return;
        }
        if (this.excelType == ExcelTypeEnum.CSV) {
            throw new ExcelGenerateException("csv cannot use template.");
        }
        byte[] templateFileByte = null;
        if (writeWorkbook.getTemplateFile() != null) {
            templateFileByte = FileUtils.readFileToByteArray(writeWorkbook.getTemplateFile());
        } else if (writeWorkbook.getTemplateInputStream() != null) {
            try {
                templateFileByte = IoUtils.toByteArray(writeWorkbook.getTemplateInputStream());
            } finally {
                if (autoCloseStream) {
                    writeWorkbook.getTemplateInputStream().close();
                }
            }
        }
        this.tempTemplateInputStream = new ByteArrayInputStream(templateFileByte);
    }
