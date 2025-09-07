    @SuppressWarnings("unchecked")
    public static <T> T valueOf(Class<T> t, String value) {
        if (ArthasCheckUtils.isIn(t, int.class, Integer.class)) {
            return (T) Integer.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, long.class, Long.class)) {
            return (T) Long.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, double.class, Double.class)) {
            return (T) Double.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, float.class, Float.class)) {
            return (T) Float.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, char.class, Character.class)) {
            return (T) Character.valueOf(value.charAt(0));
        } else if (ArthasCheckUtils.isIn(t, byte.class, Byte.class)) {
            return (T) Byte.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, boolean.class, Boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, short.class, Short.class)) {
            return (T) Short.valueOf(value);
        } else if (ArthasCheckUtils.isIn(t, String.class)) {
            return (T) value;
        } else {
            return null;
        }
    }
