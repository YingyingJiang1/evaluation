    @Override
    public void draw(CommandProcess process, MemoryModel result) {
        TableElement table = drawMemoryInfo(result.getMemoryInfo());
        process.write(RenderUtil.render(table, process.width()));
    }
