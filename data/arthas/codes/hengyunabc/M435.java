    public Ansi format(String pattern, Object... args) {
        flushAttributes();
        builder.append(String.format(pattern, args));
        return this;
    }
