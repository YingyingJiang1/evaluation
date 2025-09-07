    private boolean doOutputStreamEncrypt07() throws Exception {
        if (StringUtils.isEmpty(writeWorkbookHolder.getPassword())
            || !ExcelTypeEnum.XLSX.equals(writeWorkbookHolder.getExcelType())) {
            return false;
        }
        if (writeWorkbookHolder.getFile() != null) {
            return false;
        }
        File tempXlsx = FileUtils.createTmpFile(UUID.randomUUID() + ".xlsx");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempXlsx);
        try {
            writeWorkbookHolder.getWorkbook().write(tempFileOutputStream);
        } finally {
            try {
                writeWorkbookHolder.getWorkbook().close();
                tempFileOutputStream.close();
            } catch (Exception e) {
                if (!tempXlsx.delete()) {
                    throw new ExcelGenerateException("Can not delete temp File!");
                }
                throw e;
            }
        }
        try (POIFSFileSystem fileSystem = openFileSystemAndEncrypt(tempXlsx)) {
            fileSystem.writeFilesystem(writeWorkbookHolder.getOutputStream());
        } finally {
            if (!tempXlsx.delete()) {
                throw new ExcelGenerateException("Can not delete temp File!");
            }
        }
        return true;
    }
