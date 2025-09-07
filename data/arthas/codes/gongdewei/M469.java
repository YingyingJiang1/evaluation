    private void drawResources(CommandProcess process, List<String> resources) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        for (String resource : resources) {
            table.row(resource);
        }
        process.write(RenderUtil.render(table, process.width()) + "\n");
        process.write(com.taobao.arthas.core.util.Constants.EMPTY_STRING);
    }
