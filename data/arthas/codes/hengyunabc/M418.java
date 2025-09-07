    public static Ansi ansi() {
        if (isEnabled()) {
            return new Ansi();
        } else {
            return new NoAnsi();
        }
    }
