    private void drawDumpedClasses(CommandProcess process, List<DumpClassVO> classVOs) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(new LabelElement("HASHCODE").style(Decoration.bold.bold()),
                new LabelElement("CLASSLOADER").style(Decoration.bold.bold()),
                new LabelElement("LOCATION").style(Decoration.bold.bold()));

        for (DumpClassVO clazz : classVOs) {
            table.row(label(clazz.getClassLoaderHash()).style(Decoration.bold.fg(Color.red)),
                    TypeRenderUtils.drawClassLoader(clazz),
                    label(clazz.getLocation()).style(Decoration.bold.fg(Color.red)));
        }

        process.write(RenderUtil.render(table, process.width()))
                .write(com.taobao.arthas.core.util.Constants.EMPTY_STRING);
    }
