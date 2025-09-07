    private static String renderVMOptions(List<VMOption> diagnosticOptions, int width) {
        TableElement table = new TableElement(1, 1, 1, 1).leftCellPadding(1).rightCellPadding(1);
        table.row(true, label("KEY").style(Decoration.bold.bold()),
                label("VALUE").style(Decoration.bold.bold()),
                label("ORIGIN").style(Decoration.bold.bold()),
                label("WRITEABLE").style(Decoration.bold.bold()));

        for (VMOption option : diagnosticOptions) {
            table.row(option.getName(), option.getValue(), "" + option.getOrigin(), "" + option.isWriteable());
        }

        return RenderUtil.render(table, width);
    }
