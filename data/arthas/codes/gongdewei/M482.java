    private static Element mainHelp(List<CommandVO> commands) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(new LabelElement("NAME").style(Style.style(Decoration.bold)), new LabelElement("DESCRIPTION"));
        for (CommandVO commandVO : commands) {
            table.add(row().add(label(commandVO.getName()).style(Style.style(Color.green))).add(label(commandVO.getSummary())));
        }
        return table;
    }
