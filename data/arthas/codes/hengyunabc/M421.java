    public Ansi a(String value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
