    private static int findLastPipe(List<CliToken> lineTokens) {
        int index = -1;
        for (int i = 0; i < lineTokens.size(); i++) {
            if ("|".equals(lineTokens.get(i).value())) {
                index = i;
            }
        }
        return index;
    }
