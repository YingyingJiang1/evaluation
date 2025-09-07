    public Ansi a(int value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
