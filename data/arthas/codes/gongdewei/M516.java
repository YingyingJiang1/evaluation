    @Override
    public void draw(CommandProcess process, OptionsModel result) {
        if (result.getOptions() != null) {
            process.write(RenderUtil.render(drawShowTable(result.getOptions()), process.width()));
        } else if (result.getChangeResult() != null) {
            TableElement table = ViewRenderUtil.renderChangeResult(result.getChangeResult());
            process.write(RenderUtil.render(table, process.width()));
        }
    }
