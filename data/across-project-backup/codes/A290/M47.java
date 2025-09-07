    protected void buildChain(WriteHandler writeHandler, boolean runOwn) {
        if (writeHandler instanceof CellWriteHandler) {
            if (!runOwn) {
                if (cellHandlerExecutionChain == null) {
                    cellHandlerExecutionChain = new CellHandlerExecutionChain((CellWriteHandler)writeHandler);
                } else {
                    cellHandlerExecutionChain.addLast((CellWriteHandler)writeHandler);
                }
            }
        }
        if (writeHandler instanceof RowWriteHandler) {
            if (!runOwn) {
                if (rowHandlerExecutionChain == null) {
                    rowHandlerExecutionChain = new RowHandlerExecutionChain((RowWriteHandler)writeHandler);
                } else {
                    rowHandlerExecutionChain.addLast((RowWriteHandler)writeHandler);
                }
            }
        }
        if (writeHandler instanceof SheetWriteHandler) {
            if (!runOwn) {
                if (sheetHandlerExecutionChain == null) {
                    sheetHandlerExecutionChain = new SheetHandlerExecutionChain((SheetWriteHandler)writeHandler);
                } else {
                    sheetHandlerExecutionChain.addLast((SheetWriteHandler)writeHandler);
                }
            } else {
                if (ownSheetHandlerExecutionChain == null) {
                    ownSheetHandlerExecutionChain = new SheetHandlerExecutionChain((SheetWriteHandler)writeHandler);
                } else {
                    ownSheetHandlerExecutionChain.addLast((SheetWriteHandler)writeHandler);
                }
            }
        }
        if (writeHandler instanceof WorkbookWriteHandler) {
            if (!runOwn) {
                if (workbookHandlerExecutionChain == null) {
                    workbookHandlerExecutionChain = new WorkbookHandlerExecutionChain(
                        (WorkbookWriteHandler)writeHandler);
                } else {
                    workbookHandlerExecutionChain.addLast((WorkbookWriteHandler)writeHandler);
                }
            } else {
                if (ownWorkbookHandlerExecutionChain == null) {
                    ownWorkbookHandlerExecutionChain = new WorkbookHandlerExecutionChain(
                        (WorkbookWriteHandler)writeHandler);
                } else {
                    ownWorkbookHandlerExecutionChain.addLast((WorkbookWriteHandler)writeHandler);
                }
            }
        }
        if (!runOwn) {
            this.writeHandlerList.add(writeHandler);
        }
    }
