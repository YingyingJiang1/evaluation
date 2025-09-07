    public static Element renderMethod(MethodVO method) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(method.getDeclaringClass()))
                .row(label("method-name").style(bold.bold()), label(method.getMethodName()).style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(method.getModifier()))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(method.getAnnotations())))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(method.getParameters())))
                .row(label("return").style(bold.bold()), label(method.getReturnType()))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(method.getExceptions())))
                .row(label("classLoaderHash").style(bold.bold()), label(method.getClassLoaderHash()));
        return table;
    }
