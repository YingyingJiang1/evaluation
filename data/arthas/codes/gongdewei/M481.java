    @Override
    public void draw(CommandProcess process, HelpModel result) {
        if (result.getCommands() != null) {
            String message = RenderUtil.render(mainHelp(result.getCommands()), process.width());
            process.write(message);
        } else if (result.getDetailCommand() != null) {
            process.write(commandHelp(result.getDetailCommand().cli(), process.width()));
        }
    }
