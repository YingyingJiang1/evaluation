    public static Element renderMatchedClasses(Collection<ClassVO> matchedClasses) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(new LabelElement("NAME").style(Decoration.bold.bold()),
                new LabelElement("HASHCODE").style(Decoration.bold.bold()),
                new LabelElement("CLASSLOADER").style(Decoration.bold.bold()));

        for (ClassVO c : matchedClasses) {
            table.row(label(c.getName()),
                    label(c.getClassLoaderHash()).style(Decoration.bold.fg(Color.red)),
                    TypeRenderUtils.drawClassLoader(c));
        }
        return table;
    }
