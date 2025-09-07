    private Element drawShowTable(Collection<OptionVO> options) {
        TableElement table = new TableElement(1, 1, 2, 1, 3, 6)
                .leftCellPadding(1).rightCellPadding(1);
        table.row(true, label("LEVEL").style(Decoration.bold.bold()),
                label("TYPE").style(Decoration.bold.bold()),
                label("NAME").style(Decoration.bold.bold()),
                label("VALUE").style(Decoration.bold.bold()),
                label("SUMMARY").style(Decoration.bold.bold()),
                label("DESCRIPTION").style(Decoration.bold.bold()));

        for (final OptionVO optionVO : options) {
            table.row("" + optionVO.getLevel(),
                    optionVO.getType(),
                    optionVO.getName(),
                    optionVO.getValue(),
                    optionVO.getSummary(),
                    optionVO.getDescription());
        }
        return table;
    }
