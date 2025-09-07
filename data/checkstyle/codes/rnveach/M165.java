    private AxisIterator getPrecedingSiblingsIterator() {
        final AxisIterator result;
        if (indexAmongSiblings == 0) {
            result = EmptyIterator.ofNodes();
        }
        else {
            result = new ReverseListIterator(getPrecedingSiblings());
        }
        return result;
    }
