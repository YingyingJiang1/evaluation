    private void checkForPush() {
        // stack is full
        if (current == max) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
