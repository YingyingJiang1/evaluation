    @Override
    public String draw() {
        return new TableView(new TableView.ColumnDefine[]{
                new TableView.ColumnDefine("declaring-class".length(), false, TableView.Align.RIGHT),
                // (列数-1) * 3 + 4 = 7
                new TableView.ColumnDefine(width - "declaring-class".length() - 7, false, TableView.Align.LEFT)
        })
                .addRow("declaring-class", method.getDeclaringClass().getName())
                .addRow("method-name", method.getName())
                .addRow("modifier", StringUtils.modifier(method.getModifiers(), ','))
                .addRow("annotation", drawAnnotation())
                .addRow("parameters", drawParameters())
                .addRow("return", drawReturn())
                .addRow("exceptions", drawExceptions())
                .padding(1)
                .hasBorder(true)
                .draw();
    }
