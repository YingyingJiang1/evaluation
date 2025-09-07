    public static Ansi ansi(int size) {
        if (isEnabled()) {
            return new Ansi(size);
        } else {
            return new NoAnsi(size);
        }
    }
