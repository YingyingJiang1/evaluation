    private void recursive(int deep, boolean isLast, String prefix, TraceNode node, Callback callback) {
        callback.callback(deep, isLast, prefix, node);
        if (!isLeaf(node)) {
            List<TraceNode> children = node.getChildren();
            if (children == null) {
                return;
            }
            final int size = children.size();
            for (int index = 0; index < size; index++) {
                final boolean isLastFlag = index == size - 1;
                final String currentPrefix = isLast ? prefix + STEP_EMPTY_BOARD : prefix + STEP_HAS_BOARD;
                recursive(
                        deep + 1,
                        isLastFlag,
                        currentPrefix,
                        children.get(index),
                        callback
                );
            }
        }
    }
