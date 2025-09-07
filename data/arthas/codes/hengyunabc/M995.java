    private static int blankToken(String s, int index, List<CliToken> builder) {
        int from = index;
        while (index < s.length() && isBlank(s.charAt(index))) {
            index++;
        }
        builder.add(new CliTokenImpl(false, s.substring(from, index)));
        return index;
    }
