    public Ansi a(float value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
