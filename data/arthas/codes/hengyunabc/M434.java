    public Ansi newline() {
        flushAttributes();
        builder.append(System.getProperty("line.separator"));
        return this;
    }
