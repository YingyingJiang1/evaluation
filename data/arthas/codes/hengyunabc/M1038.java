    @Override
    public void accept(int[] codePoints) {
        // Echo
        term.echo(codePoints);
        term.getReadline().queueEvent(codePoints);
    }
