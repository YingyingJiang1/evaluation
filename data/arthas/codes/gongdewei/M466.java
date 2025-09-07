    private void drawClassLoaderStats(CommandProcess process, Map<String, ClassLoaderStat> classLoaderStats) {
        Element element = renderStat(classLoaderStats);
        process.write(RenderUtil.render(element, process.width()))
                .write(com.taobao.arthas.core.util.Constants.EMPTY_STRING);

    }
