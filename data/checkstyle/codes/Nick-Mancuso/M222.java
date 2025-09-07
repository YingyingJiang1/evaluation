    private static int getSmallestIndent(Collection<String> lines) {
        return lines.stream()
            .mapToInt(CommonUtil::indexOfNonWhitespace)
            .min()
            .orElse(0);
    }
