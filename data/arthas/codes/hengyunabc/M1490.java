    public static <E> boolean isIn(E e, E... s) {

        if (null != s) {
            for (E es : s) {
                if (isEquals(e, es)) {
                    return true;
                }
            }
        }

        return false;

    }
