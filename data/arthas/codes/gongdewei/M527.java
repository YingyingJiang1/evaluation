    @Override
    public void draw(CommandProcess process, StatusModel result) {
        if (result.getMessage() != null) {
            writeln(process, result.getMessage());
        }
    }
