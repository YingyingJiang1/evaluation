    @Override
    public void push(E e) {
        ensureCapacityInternal(current + 1);
        elementArray[++current] = e;
    }
