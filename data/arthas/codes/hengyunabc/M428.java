    public Ansi a(double value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
