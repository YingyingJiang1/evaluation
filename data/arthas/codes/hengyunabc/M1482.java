    @Override
    public void push(E e) {
        checkForPush();
        elementArray[++current] = e;
    }
