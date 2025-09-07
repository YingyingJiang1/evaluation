    public Ansi a(StringBuffer value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
