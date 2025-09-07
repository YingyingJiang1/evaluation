    @Override
    public AxisIterator iterateAxis(int axisNumber) {
        final AxisIterator result;
        switch (axisNumber) {
            case AxisInfo.ANCESTOR:
                result = new Navigator.AncestorEnumeration(this, false);
                break;
            case AxisInfo.ANCESTOR_OR_SELF:
                result = new Navigator.AncestorEnumeration(this, true);
                break;
            case AxisInfo.ATTRIBUTE:
                result = SingleNodeIterator.makeIterator(getAttributeNode());
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
            case AxisInfo.PARENT:
                result = SingleNodeIterator.makeIterator(parent);
                break;
            case AxisInfo.SELF:
                result = SingleNodeIterator.makeIterator(this);
                break;
            case AxisInfo.FOLLOWING_SIBLING:
                result = getFollowingSiblingsIterator();
                break;
            case AxisInfo.PRECEDING_SIBLING:
                result = getPrecedingSiblingsIterator();
                break;
            case AxisInfo.FOLLOWING:
                result = new FollowingIterator(this);
                break;
            case AxisInfo.PRECEDING:
                result = new PrecedingIterator(this);
                break;
            default:
                throw throwUnsupportedOperationException();
        }

        return result;
    }
