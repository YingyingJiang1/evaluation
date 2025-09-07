    public static <E> boolean isEquals(E src, E target) {

        return null == src
                && null == target
                || null != src
                && null != target
                && src.equals(target);

    }
