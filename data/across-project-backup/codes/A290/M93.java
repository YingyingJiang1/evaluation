    private void fillHyperLink(CellWriteHandlerContext cellWriteHandlerContext, HyperlinkData hyperlinkData) {
        if (hyperlinkData == null) {
            return;
        }
        Integer rowIndex = cellWriteHandlerContext.getRowIndex();
        Integer columnIndex = cellWriteHandlerContext.getColumnIndex();
        Workbook workbook = cellWriteHandlerContext.getWriteWorkbookHolder().getWorkbook();
        Cell cell = cellWriteHandlerContext.getCell();

        CreationHelper helper = workbook.getCreationHelper();
        Hyperlink hyperlink = helper.createHyperlink(StyleUtil.getHyperlinkType(hyperlinkData.getHyperlinkType()));
        hyperlink.setAddress(hyperlinkData.getAddress());
        hyperlink.setFirstRow(StyleUtil.getCellCoordinate(rowIndex, hyperlinkData.getFirstRowIndex(),
            hyperlinkData.getRelativeFirstRowIndex()));
        hyperlink.setFirstColumn(StyleUtil.getCellCoordinate(columnIndex, hyperlinkData.getFirstColumnIndex(),
            hyperlinkData.getRelativeFirstColumnIndex()));
        hyperlink.setLastRow(StyleUtil.getCellCoordinate(rowIndex, hyperlinkData.getLastRowIndex(),
            hyperlinkData.getRelativeLastRowIndex()));
        hyperlink.setLastColumn(StyleUtil.getCellCoordinate(columnIndex, hyperlinkData.getLastColumnIndex(),
            hyperlinkData.getRelativeLastColumnIndex()));
        cell.setHyperlink(hyperlink);
    }
