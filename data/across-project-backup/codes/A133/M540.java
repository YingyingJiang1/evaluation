    @Override
    public void draw(CommandProcess process, RetransformModel result) {
        // 匹配到多个 classloader
        if (result.getMatchedClassLoaders() != null) {
            process.write("Matched classloaders: \n");
            ClassLoaderView.drawClassLoaders(process, result.getMatchedClassLoaders(), false);
            process.write("\n");
            return;
        }

        // retransform -d
        if (result.getDeletedRetransformEntry() != null) {
            process.write("Delete RetransformEntry by id success. id: " + result.getDeletedRetransformEntry().getId());
            process.write("\n");
            return;
        }

        // retransform -l
        if (result.getRetransformEntries() != null) {
            // header
            TableElement table = new TableElement(1, 1, 1, 1, 1).rightCellPadding(1);
            table.add(new RowElement().style(Decoration.bold.bold()).add("Id", "ClassName", "TransformCount", "LoaderHash",
                    "LoaderClassName"));

            for (RetransformEntry entry : result.getRetransformEntries()) {
                table.row("" + entry.getId(), "" + entry.getClassName(), "" + entry.getTransformCount(), "" + entry.getHashCode(),
                        "" + entry.getClassLoaderClass());
            }

            process.write(RenderUtil.render(table));
            return;
        }

        // retransform /tmp/Demo.class
        if (result.getRetransformClasses() != null) {
            StringBuilder sb = new StringBuilder();
            for (String aClass : result.getRetransformClasses()) {
                sb.append(aClass).append("\n");
            }
            process.write("retransform success, size: " + result.getRetransformCount()).write(", classes:\n")
                    .write(sb.toString());
        }

    }
