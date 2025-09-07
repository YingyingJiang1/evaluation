    private Ansi _appendEscapeSequence(char command, Object... options) {
        builder.append(FIRST_ESC_CHAR);
        builder.append(SECOND_ESC_CHAR);
        int size = options.length;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                builder.append(';');
            }
            if (options[i] != null) {
                builder.append(options[i]);
            }
        }
        builder.append(command);
        return this;
    }
