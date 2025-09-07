    @Override
    public E pop() {
        try {
            checkForPopOrPeek();
            E res = (E) elementArray[current];
            elementArray[current] = null;
            current--;
            return res;
        } finally {
            if (current == EMPTY_INDEX && elementArray.length > DEFAULT_STACK_DEEP) {
                elementArray = new Object[DEFAULT_STACK_DEEP];
                if (logger.isDebugEnabled()) {
                    logger.debug("resize GaStack to default length for thread: " + Thread.currentThread().getName());
                }
            }
        }
    }
