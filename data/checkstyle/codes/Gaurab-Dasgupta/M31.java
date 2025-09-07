    private static String constructSubTreeText(DetailNode node, int childLeftLimit,
                                               int childRightLimit) {
        DetailNode detailNode = node;

        final Deque<DetailNode> stack = new ArrayDeque<>();
        stack.addFirst(detailNode);
        final Set<DetailNode> visited = new HashSet<>();
        final StringBuilder result = new StringBuilder(1024);
        while (!stack.isEmpty()) {
            detailNode = stack.removeFirst();

            if (visited.add(detailNode)) {
                final String childText = detailNode.getText();
                if (detailNode.getType() != JavadocTokenTypes.LEADING_ASTERISK
                        && !TOKEN_TEXT_PATTERN.matcher(childText).matches()) {
                    result.insert(0, childText);
                }
            }

            for (DetailNode child : detailNode.getChildren()) {
                if (child.getParent().equals(node)
                        && (child.getIndex() < childLeftLimit
                        || child.getIndex() > childRightLimit)) {
                    continue;
                }
                if (!visited.contains(child)) {
                    stack.addFirst(child);
                }
            }
        }
        return result.toString().trim();
    }
