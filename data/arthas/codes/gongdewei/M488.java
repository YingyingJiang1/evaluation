    @Override
    public void draw(CommandProcess process, VMOptionModel result) {
        if (result.getVmOptions() != null) {
            process.write(renderVMOptions(result.getVmOptions(), process.width()));
        } else if (result.getChangeResult() != null) {
            TableElement table = ViewRenderUtil.renderChangeResult(result.getChangeResult());
            process.write(RenderUtil.render(table, process.width()));
        }
    }
