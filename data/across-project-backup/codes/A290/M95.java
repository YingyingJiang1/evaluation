    protected void fillImage(CellWriteHandlerContext cellWriteHandlerContext, List<ImageData> imageDataList) {
        if (CollectionUtils.isEmpty(imageDataList)) {
            return;
        }
        Integer rowIndex = cellWriteHandlerContext.getRowIndex();
        Integer columnIndex = cellWriteHandlerContext.getColumnIndex();
        Sheet sheet = cellWriteHandlerContext.getWriteSheetHolder().getSheet();
        Workbook workbook = cellWriteHandlerContext.getWriteWorkbookHolder().getWorkbook();

        Drawing<?> drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        for (ImageData imageData : imageDataList) {
            int index = workbook.addPicture(imageData.getImage(),
                FileTypeUtils.getImageTypeFormat(imageData.getImage()));
            ClientAnchor anchor = helper.createClientAnchor();
            if (imageData.getTop() != null) {
                anchor.setDy1(StyleUtil.getCoordinate(imageData.getTop()));
            }
            if (imageData.getRight() != null) {
                anchor.setDx2(-StyleUtil.getCoordinate(imageData.getRight()));
            }
            if (imageData.getBottom() != null) {
                anchor.setDy2(-StyleUtil.getCoordinate(imageData.getBottom()));
            }
            if (imageData.getLeft() != null) {
                anchor.setDx1(StyleUtil.getCoordinate(imageData.getLeft()));
            }
            anchor.setRow1(StyleUtil.getCellCoordinate(rowIndex, imageData.getFirstRowIndex(),
                imageData.getRelativeFirstRowIndex()));
            anchor.setCol1(StyleUtil.getCellCoordinate(columnIndex, imageData.getFirstColumnIndex(),
                imageData.getRelativeFirstColumnIndex()));
            anchor.setRow2(StyleUtil.getCellCoordinate(rowIndex, imageData.getLastRowIndex(),
                imageData.getRelativeLastRowIndex()) + 1);
            anchor.setCol2(StyleUtil.getCellCoordinate(columnIndex, imageData.getLastColumnIndex(),
                imageData.getRelativeLastColumnIndex()) + 1);
            if (imageData.getAnchorType() != null) {
                anchor.setAnchorType(imageData.getAnchorType().getValue());
            }
            drawing.createPicture(anchor, index);
        }
    }
