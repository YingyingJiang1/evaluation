    private static void tokenize(String s, int index, List<CliToken> builder) {
        while (index < s.length()) {
            char c = s.charAt(index);
            switch (c) {
                case ' ':
                case '\t':
                    index = blankToken(s, index, builder);
                    break;
                default:
                    index = textToken(s, index, builder);
                    break;
            }
        }
    }
