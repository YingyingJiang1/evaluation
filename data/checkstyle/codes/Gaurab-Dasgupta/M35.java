    private static Optional<DetailNode> getFirstChildOfMatchingText(DetailNode node,
                                                                    Pattern pattern) {
        return Arrays.stream(node.getChildren())
                .filter(child -> pattern.matcher(child.getText()).matches())
                .findFirst();
    }
