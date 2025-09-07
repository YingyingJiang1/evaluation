    @Override
    protected boolean matchesExactly(String pkg, String fileName) {
        final boolean result;
        if (fileName == null) {
            result = false;
        }
        else if (regex) {
            result = patternForExactMatch.matcher(fileName).matches();
        }
        else {
            result = name.equals(fileName);
        }
        return result;
    }
