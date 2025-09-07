    public static Ansi ansi(StringBuilder builder) {
        if (isEnabled()) {
            return new Ansi(builder);
        } else {
            return new NoAnsi(builder);
        }
    }
