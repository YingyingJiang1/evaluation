    public Ansi a(char value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
