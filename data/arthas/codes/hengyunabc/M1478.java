    @Override
    public E peek() {
        checkForPopOrPeek();
        return (E) elementArray[current];
    }
