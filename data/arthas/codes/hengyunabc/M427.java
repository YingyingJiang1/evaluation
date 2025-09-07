    public Ansi a(CharSequence value) {
        flushAttributes();
        builder.append(value);
        return this;
    }
