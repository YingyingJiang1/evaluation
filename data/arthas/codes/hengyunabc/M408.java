    private boolean isAnyBorder(int... borders) {
        if (null == borders) {
            return false;
        }
        for (int b : borders) {
            if ((this.borders & b) == b) {
                return true;
            }
        }
        return false;
    }
