    private boolean runInBackground(List<CliToken> tokens) {
        boolean runInBackground = false;
        CliToken last = TokenUtils.findLastTextToken(tokens);
        if (last != null && "&".equals(last.value())) {
            runInBackground = true;
            tokens.remove(last);
        }
        return runInBackground;
    }
