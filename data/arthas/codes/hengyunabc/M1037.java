    @Override
    public void accept(String line) {
        term.setInReadline(false);
        lineHandler.handle(line);
    }
