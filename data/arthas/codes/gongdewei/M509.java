    private static TableElement createTable() {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(true, label("NAME").style(Decoration.bold.bold()),
                label("VALUE").style(Decoration.bold.bold()));
        return table;
    }
