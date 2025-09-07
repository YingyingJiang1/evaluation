    private void dealStyle(List<WriteHandler> handlerList) {
        WriteHandler styleStrategy = new AbstractVerticalCellStyleStrategy() {
            @Override
            public int order() {
                return OrderConstant.ANNOTATION_DEFINE_STYLE;
            }

            @Override
            protected WriteCellStyle headCellStyle(CellWriteHandlerContext context) {
                Head head = context.getHeadData();
                if (head == null) {
                    return null;
                }
                return WriteCellStyle.build(head.getHeadStyleProperty(), head.getHeadFontProperty());
            }

            @Override
            protected WriteCellStyle contentCellStyle(CellWriteHandlerContext context) {
                ExcelContentProperty excelContentProperty = context.getExcelContentProperty();
                return WriteCellStyle.build(excelContentProperty.getContentStyleProperty(),
                    excelContentProperty.getContentFontProperty());
            }
        };
        handlerList.add(styleStrategy);
    }
