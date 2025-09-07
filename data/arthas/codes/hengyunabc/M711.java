    static TableElement fillTableHeader(TableElement table) {
        LabelElement[] headers = new LabelElement[TABLE_COL_TITLE.length];
        for (int i = 0; i < TABLE_COL_TITLE.length; ++i) {
            headers[i] = label(TABLE_COL_TITLE[i]).style(Decoration.bold.bold());
        }
        table.row(true, headers);
        return table;
    }
