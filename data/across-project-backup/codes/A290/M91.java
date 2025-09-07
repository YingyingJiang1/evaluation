    protected void converterAndSet(CellWriteHandlerContext cellWriteHandlerContext) {

        WriteCellData<?> cellData = convert(cellWriteHandlerContext);
        cellWriteHandlerContext.setCellDataList(ListUtils.newArrayList(cellData));
        cellWriteHandlerContext.setFirstCellData(cellData);

        WriteHandlerUtils.afterCellDataConverted(cellWriteHandlerContext);

        // Fill in picture information
        fillImage(cellWriteHandlerContext, cellData.getImageDataList());

        // Fill in comment information
        fillComment(cellWriteHandlerContext, cellData.getCommentData());

        // Fill in hyper link information
        fillHyperLink(cellWriteHandlerContext, cellData.getHyperlinkData());

        // Fill in formula information
        fillFormula(cellWriteHandlerContext, cellData.getFormulaData());

        // Fill index
        cellData.setRowIndex(cellWriteHandlerContext.getRowIndex());
        cellData.setColumnIndex(cellWriteHandlerContext.getColumnIndex());

        if (cellData.getType() == null) {
            cellData.setType(CellDataTypeEnum.EMPTY);
        }
        Cell cell = cellWriteHandlerContext.getCell();
        switch (cellData.getType()) {
            case STRING:
                cell.setCellValue(cellData.getStringValue());
                return;
            case BOOLEAN:
                cell.setCellValue(cellData.getBooleanValue());
                return;
            case NUMBER:
                cell.setCellValue(cellData.getNumberValue().doubleValue());
                return;
            case DATE:
                cell.setCellValue(cellData.getDateValue());
                return;
            case RICH_TEXT_STRING:
                cell.setCellValue(StyleUtil
                    .buildRichTextString(writeContext.writeWorkbookHolder(), cellData.getRichTextStringDataValue()));
                return;
            case EMPTY:
                return;
            default:
                throw new ExcelWriteDataConvertException(cellWriteHandlerContext,
                    "Not supported data:" + cellWriteHandlerContext.getOriginalValue() + " return type:"
                        + cellData.getType()
                        + "at row:" + cellWriteHandlerContext.getRowIndex());
        }

    }
