    public static List<WriteHandler> loadDefaultHandler(Boolean useDefaultStyle, ExcelTypeEnum excelType) {
        List<WriteHandler> handlerList = new ArrayList<>();
        switch (excelType) {
            case XLSX:
                handlerList.add(new DimensionWorkbookWriteHandler());
                handlerList.add(new DefaultRowWriteHandler());
                handlerList.add(new FillStyleCellWriteHandler());
                if (useDefaultStyle) {
                    handlerList.add(new DefaultStyle());
                }
                break;
            case XLS:
                handlerList.add(new DefaultRowWriteHandler());
                handlerList.add(new FillStyleCellWriteHandler());
                if (useDefaultStyle) {
                    handlerList.add(new DefaultStyle());
                }
                break;
            case CSV:
                handlerList.add(new DefaultRowWriteHandler());
                handlerList.add(new FillStyleCellWriteHandler());
                break;
            default:
                break;
        }
        return handlerList;
    }
