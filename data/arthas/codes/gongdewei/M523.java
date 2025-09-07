    String drawRuntimeInfoAndTomcatInfo(TableElement runtimeInfoTable, TableElement tomcatInfoTable, int width, int height) {
        if (height <= 0) {
            return "";
        }
        TableElement resultTable = new TableElement(1, 1);
        if (tomcatInfoTable != null) {
            resultTable.row(runtimeInfoTable, tomcatInfoTable);
        } else {
            resultTable = runtimeInfoTable;
        }
        return RenderUtil.render(resultTable, width, height);
    }
