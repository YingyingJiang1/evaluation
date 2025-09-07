    private static Optional<Integer> getAsteriskColumnNumber(String line) {
        final Pattern pattern = Pattern.compile("^(\\s*)\\*");
        final Matcher matcher = pattern.matcher(line);

        // We may not always have a leading asterisk because a javadoc line can start with
        // a non-whitespace character or the javadoc line can be empty.
        // In such cases, there is no leading asterisk and Optional will be empty.
        return Optional.of(matcher)
                .filter(Matcher::find)
                .map(matcherInstance -> matcherInstance.group(1))
                .map(groupLength -> groupLength.length() + 1);
    }
