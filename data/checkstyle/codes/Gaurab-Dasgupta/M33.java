    private static Optional<DetailNode> getFirstChildOfType(DetailNode node, int tokenType,
                                                            int offset) {
        return Arrays.stream(node.getChildren())
                .filter(child -> child.getIndex() >= offset && child.getType() == tokenType)
                .findFirst();
    }
