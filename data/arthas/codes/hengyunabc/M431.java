    public Ansi a(long value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
