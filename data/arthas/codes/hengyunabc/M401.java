    private void recursive(int deep, boolean isLast, String prefix, Node node, Callback callback) {
        callback.callback(deep, isLast, prefix, node);
        if (!node.isLeaf()) {
            final int size = node.children.size();
            for (int index = 0; index < size; index++) {
                final boolean isLastFlag = index == size - 1;
                final String currentPrefix = isLast ? prefix + STEP_EMPTY_BOARD : prefix + STEP_HAS_BOARD;
                recursive(
                        deep + 1,
                        isLastFlag,
                        currentPrefix,
                        node.children.get(index),
                        callback
                );
            }
        }
    }
