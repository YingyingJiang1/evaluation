    private void ensureCapacityInternal(int expectDeep) {
        final int currentStackSize = elementArray.length;
        if (elementArray.length <= expectDeep) {
            if (logger.isDebugEnabled()) {
                logger.debug("resize GaStack to double length: " + currentStackSize * 2 + " for thread: "
                        + Thread.currentThread().getName());
            }
            final Object[] newElementArray = new Object[currentStackSize * 2];
            arraycopy(elementArray, 0, newElementArray, 0, currentStackSize);
            this.elementArray = newElementArray;
        }
    }
