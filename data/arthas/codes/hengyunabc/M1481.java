    @Override
    public E pop() {
        checkForPopOrPeek();
        return (E) elementArray[current--];
    }
