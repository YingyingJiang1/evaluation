    private int getRegexCompileFlags() {
        final int result;

        if (matchAcrossLines) {
            result = Pattern.DOTALL;
        }
        else {
            result = Pattern.MULTILINE;
        }

        return result;
    }
