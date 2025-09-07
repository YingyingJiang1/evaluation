    public static void drawClassLoaders(CommandProcess process, Collection<ClassLoaderVO> classLoaders, boolean isTree) {
        Element element = isTree ? renderTree(classLoaders) : renderTable(classLoaders);
        process.write(RenderUtil.render(element, process.width()))
                .write(com.taobao.arthas.core.util.Constants.EMPTY_STRING);
    }
