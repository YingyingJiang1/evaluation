    @Override
    public AxisIterator iterateAxis(int axisNumber) {
        final AxisIterator result;
        switch (axisNumber) {
            case AxisInfo.ANCESTOR:
            case AxisInfo.PARENT:
            case AxisInfo.FOLLOWING:
            case AxisInfo.FOLLOWING_SIBLING:
            case AxisInfo.PRECEDING:
            case AxisInfo.PRECEDING_SIBLING:
                result = EmptyIterator.ofNodes();
                break;
            case AxisInfo.ANCESTOR_OR_SELF:
            case AxisInfo.SELF:
                result = SingleNodeIterator.makeIterator(this);
                break;
            case AxisInfo.CHILD:
                if (hasChildNodes()) {
                    result = new ArrayIterator.OfNodes<>(
                            getChildren().toArray(EMPTY_ABSTRACT_NODE_ARRAY));
                }
                else {
                    result = EmptyIterator.ofNodes();
                }
                break;
            case AxisInfo.DESCENDANT:
                if (hasChildNodes()) {
                    result = new DescendantIterator(this, DescendantIterator.StartWith.CHILDREN);
                }
                else {
                    result = EmptyIterator.ofNodes();
                }
                break;
            case AxisInfo.DESCENDANT_OR_SELF:
                result = new DescendantIterator(this, DescendantIterator.StartWith.CURRENT_NODE);
                break;
            default:
                throw throwUnsupportedOperationException();
        }
        return result;
    }
