    private String renderCollectionValue(Collection<String> strings) {
        final StringBuilder colSB = new StringBuilder();
        if (strings.isEmpty()) {
            colSB.append("[]");
        } else {
            for (String str : strings) {
                colSB.append(str).append("\n");
            }
        }
        return colSB.toString();
    }
