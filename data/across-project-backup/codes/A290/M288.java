    public static void createWorkBook(WriteWorkbookHolder writeWorkbookHolder) throws IOException {
        switch (writeWorkbookHolder.getExcelType()) {
            case XLSX:
                if (writeWorkbookHolder.getTempTemplateInputStream() != null) {
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(writeWorkbookHolder.getTempTemplateInputStream());
                    writeWorkbookHolder.setCachedWorkbook(xssfWorkbook);
                    if (writeWorkbookHolder.getInMemory()) {
                        writeWorkbookHolder.setWorkbook(xssfWorkbook);
                    } else {
                        writeWorkbookHolder.setWorkbook(new SXSSFWorkbook(xssfWorkbook));
                    }
                    return;
                }
                Workbook workbook;
                if (writeWorkbookHolder.getInMemory()) {
                    workbook = new XSSFWorkbook();
                } else {
                    workbook = new SXSSFWorkbook();
                }
                writeWorkbookHolder.setCachedWorkbook(workbook);
                writeWorkbookHolder.setWorkbook(workbook);
                return;
            case XLS:
                HSSFWorkbook hssfWorkbook;
                if (writeWorkbookHolder.getTempTemplateInputStream() != null) {
                    hssfWorkbook = new HSSFWorkbook(
                        new POIFSFileSystem(writeWorkbookHolder.getTempTemplateInputStream()));
                } else {
                    hssfWorkbook = new HSSFWorkbook();
                }
                writeWorkbookHolder.setCachedWorkbook(hssfWorkbook);
                writeWorkbookHolder.setWorkbook(hssfWorkbook);
                if (writeWorkbookHolder.getPassword() != null) {
                    Biff8EncryptionKey.setCurrentUserPassword(writeWorkbookHolder.getPassword());
                    hssfWorkbook.writeProtectWorkbook(writeWorkbookHolder.getPassword(), StringUtils.EMPTY);
                }
                return;
            case CSV:
                CsvWorkbook csvWorkbook = new CsvWorkbook(new PrintWriter(
                    new OutputStreamWriter(writeWorkbookHolder.getOutputStream(), writeWorkbookHolder.getCharset())),
                    writeWorkbookHolder.getGlobalConfiguration().getLocale(),
                    writeWorkbookHolder.getGlobalConfiguration().getUse1904windowing(),
                    writeWorkbookHolder.getGlobalConfiguration().getUseScientificFormat(),
                    writeWorkbookHolder.getCharset(),
                    writeWorkbookHolder.getWithBom());
                writeWorkbookHolder.setCachedWorkbook(csvWorkbook);
                writeWorkbookHolder.setWorkbook(csvWorkbook);
                return;
            default:
                throw new UnsupportedOperationException("Wrong excel type.");
        }

    }
