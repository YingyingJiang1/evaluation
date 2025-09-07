    private AxisIterator getFollowingSiblingsIterator() {
        final AxisIterator result;
        if (indexAmongSiblings == parent.getChildren().size() - 1) {
            result = EmptyIterator.ofNodes();
        }
        else {
            result = new ArrayIterator.OfNodes<>(
                    getFollowingSiblings().toArray(EMPTY_ABSTRACT_NODE_ARRAY));
        }
        return result;
    }
