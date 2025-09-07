    private String drawClassInfo() {
        final CodeSource cs = clazz.getProtectionDomain().getCodeSource();

        final TableView view = new TableView(new TableView.ColumnDefine[]{
                new TableView.ColumnDefine("isAnonymousClass".length(), false, TableView.Align.RIGHT),
                // (列数-1) * 3 + 4 = 7
                new TableView.ColumnDefine(width - "isAnonymousClass".length() - 7, false, TableView.Align.LEFT)
        })
                .addRow("class-info", StringUtils.classname(clazz))
                .addRow("code-source", getCodeSource(cs))
                .addRow("name", StringUtils.classname(clazz))
                .addRow("isInterface", clazz.isInterface())
                .addRow("isAnnotation", clazz.isAnnotation())
                .addRow("isEnum", clazz.isEnum())
                .addRow("isAnonymousClass", clazz.isAnonymousClass())
                .addRow("isArray", clazz.isArray())
                .addRow("isLocalClass", clazz.isLocalClass())
                .addRow("isMemberClass", clazz.isMemberClass())
                .addRow("isPrimitive", clazz.isPrimitive())
                .addRow("isSynthetic", clazz.isSynthetic())
                .addRow("simple-name", clazz.getSimpleName())
                .addRow("modifier", StringUtils.modifier(clazz.getModifiers(), ','))
                .addRow("annotation", drawAnnotation())
                .addRow("interfaces", drawInterface())
                .addRow("super-class", drawSuperClass())
                .addRow("class-loader", drawClassLoader());

        if (isPrintField) {
            view.addRow("fields", drawField());
        }

        return view.hasBorder(true).padding(1).draw();
    }
