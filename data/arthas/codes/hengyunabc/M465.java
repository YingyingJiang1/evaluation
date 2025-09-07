    private void drawUrlStats(CommandProcess process, Map<ClassLoaderVO, ClassLoaderUrlStat> urlStats) {
        for (Entry<ClassLoaderVO, ClassLoaderUrlStat> entry : urlStats.entrySet()) {
            ClassLoaderVO classLoaderVO = entry.getKey();
            ClassLoaderUrlStat urlStat = entry.getValue();

            // 忽略 sun.reflect.DelegatingClassLoader 等动态ClassLoader
            if (urlStat.getUsedUrls().isEmpty() && urlStat.getUnUsedUrls().isEmpty()) {
                continue;
            }

            TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
            table.row(new LabelElement(classLoaderVO.getName() + ", hash:" + classLoaderVO.getHash())
                    .style(Decoration.bold.bold()));
            Collection<String> usedUrls = urlStat.getUsedUrls();
            table.row(new LabelElement("Used URLs:").style(Decoration.bold.bold()));
            for (String url : usedUrls) {
                table.row(url);
            }
            Collection<String> UnnsedUrls = urlStat.getUnUsedUrls();
            table.row(new LabelElement("Unused URLs:").style(Decoration.bold.bold()));
            for (String url : UnnsedUrls) {
                table.row(url);
            }
            process.write(RenderUtil.render(table, process.width()))
                    .write("\n");
        }
    }
