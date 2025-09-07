    public Ansi a(boolean value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
