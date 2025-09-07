    public static Element renderConstructor(MethodVO constructor) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(constructor.getDeclaringClass()))
                .row(label("constructor-name").style(bold.bold()), label("<init>").style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(constructor.getModifier()))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(constructor.getAnnotations())))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(constructor.getParameters())))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(constructor.getExceptions())))
                .row(label("classLoaderHash").style(bold.bold()), label(constructor.getClassLoaderHash()));
        return table;
    }
