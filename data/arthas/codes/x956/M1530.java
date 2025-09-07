    @Override
    public boolean isEndOfInput() throws Exception {
        if (input.isEndOfInput()) {
            // Only end of input after last HTTP chunk has been sent
            return true;
        } else {
            return false;
        }
    }
