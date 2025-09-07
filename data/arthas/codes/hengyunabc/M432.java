    public Ansi a(Object value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
